package br.com.automica.api.whatsapp.modules.conversa.domain.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.mensagem.request.MensagemRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Conversa;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Mensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.DirecaoMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.StatusMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.TipoMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.exceptions.RegraNegocioException;
import br.com.automica.api.whatsapp.modules.conversa.domain.gateways.WhatsAppGateway;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.ConversaRepository;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.MensagemRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class MensagemService {

    @Autowired
    private ConversaRepository conversaRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private WhatsAppGateway whatsAppGateway;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public String enviarMensagemTexto(MensagemRequestDto request) {
        var idConversa = request.getIdConversa();

        if (idConversa == null || !conversaRepository.existsById(idConversa)) {
            throw new RegraNegocioException("Conversa não encontrada. Envie um template para iniciar a conversa.");
        }

        Conversa conversa = conversaRepository.findById(idConversa).get();

        var mensagem = new Mensagem();
        mensagem.setIdMensagem(UUID.randomUUID().toString());
        mensagem.setConversa(conversa);
        mensagem.setBody(request.getBody());
        mensagem.setDirecaoMensagem(DirecaoMensagem.SAIDA);
        mensagem.setTipoMensagem(TipoMensagem.TEXTO);
        mensagem.setRecebidaEm(Instant.now());

        var mensagemRequest = new MensagemRequestDto();
        mensagemRequest.setIdConversa(mensagem.getConversa().getIdConversa());
        mensagemRequest.setDestinatario(mensagem.getConversa().getWaId());
        mensagemRequest.setTipo(mensagem.getTipoMensagem().name());
        mensagemRequest.setBody(mensagem.getBody());

        String response = "";

        try {
            response = whatsAppGateway.enviarMensagem(mensagemRequest);
            JsonNode root = objectMapper.readTree(response);
            var idMeta = root
                    .get("messages")
                    .get(0)
                    .get("id").stringValue();
            mensagem.setIdMensagem(idMeta);
            mensagem.setStatus(StatusMensagem.ENVIADA);
        } catch (Exception e) {
            mensagem.setIdMensagem(UUID.randomUUID().toString());
            mensagem.setStatus(StatusMensagem.PROCESSADA);
            response = "Erro ao enviar mensagem: WAMID da meta nulo.";
            System.out.println("Erro ao enviar mensagem: WAMID da meta nulo. " + e.getMessage());
        }
        mensagemRepository.save(mensagem);
        return response;
    }
}
