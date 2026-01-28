package br.com.automica.api.whatsapp.modules.conversa.domain.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.mensagem.request.MensagemRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Conversa;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Mensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.DirecaoMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.StatusMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.TipoMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.gateways.WhatsAppGateway;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.ConversaRepository;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.MensagemRepository;

@Service
public class MensagemService {

    @Autowired
    private ConversaRepository conversaRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private WhatsAppGateway whatsAppGateway;

    
	public String enviarTemplate(String destinatario) {

		var response = whatsAppGateway.enviarMensagemTemplate(destinatario);

		return response;
	}

    public void enviarMensagemTexto(MensagemRequestDto request) {
        var idConversa = request.getIdConversa();
        Conversa conversa = conversaRepository.findById(idConversa).get();

        if (conversa != null) {
            var mensagem = new Mensagem();
            mensagem.setIdMensagem(UUID.randomUUID().toString());
            mensagem.setConversa(conversa);
            mensagem.setBody(request.getBody());
            mensagem.setDirecaoMensagem(DirecaoMensagem.SAIDA);
            mensagem.setStatus(StatusMensagem.ENVIADA);
            mensagem.setTipoMensagem(TipoMensagem.TEXTO);
            mensagemRepository.save(mensagem);

            var mensagemRequest = new MensagemRequestDto();
            mensagemRequest.setIdConversa(mensagem.getConversa().getIdConversa());
            mensagemRequest.setDestinatario(mensagem.getConversa().getWaId());
            mensagemRequest.setTipo(mensagem.getTipoMensagem().name());
            mensagemRequest.setBody(mensagem.getBody());

            whatsAppGateway.enviarMensagem(mensagemRequest);
        }
    }

}
