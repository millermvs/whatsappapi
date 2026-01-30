package br.com.automica.api.whatsapp.modules.conversa.domain.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Conversa;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Mensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.DirecaoMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.StatusMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.enums.TipoMensagem;
import br.com.automica.api.whatsapp.modules.conversa.domain.gateways.WhatsAppGateway;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.ConversaRepository;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.MensagemRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class ConversaService {

    @Autowired
    private ConversaRepository conversaRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private WhatsAppGateway whatsAppGateway;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Metodo para criar conversa com mensagem de template da Meta (usado pelo
    // controller)
    public String criarConversaSistema(String waid, String phoneNumberId, String wabaId) {
        var conversaExistente = conversaRepository.findByWaIdAndPhoneNumberId(waid, phoneNumberId);
        String response = "";
        // Conversa ja existe para waId e phoneNumberId
        if (conversaExistente != null) {
            try {
                String enviado = whatsAppGateway.enviarMensagemTemplate(waid);
                JsonNode payload = objectMapper.readTree(enviado);

                JsonNode messages = payload.path("messages");

                for (JsonNode message : messages) {
                    String idMeta = message.path("id").stringValue();

                    var mensagem = new Mensagem();
                    mensagem.setIdMensagem(idMeta);
                    mensagem.setConversa(conversaExistente);
                    mensagem.setDirecaoMensagem(DirecaoMensagem.SAIDA);
                    mensagem.setTipoMensagem(TipoMensagem.TEXTO);
                    mensagem.setBody("Template enviado pela Meta, aguarde a resposta do destinatário.");
                    mensagem.setEnviadaEm(Instant.now());
                    mensagem.setStatus(StatusMensagem.ENVIADA);
                    mensagem.setProcessada(false);
                    mensagemRepository.save(mensagem);
                    conversaExistente.setLastMessageAt(Instant.now());
                    conversaExistente.setLastMessageId(idMeta);
                }
                return response = "Conversa existente encontrada. Template reenviado com sucesso.";
            } catch (Exception e) {
                response = "Erro ao enviar mensagem de template na conversa existente: " + e.getMessage();
            }

            return response;
        }

        // Conversa nao existe para waId e phoneNumberId
        var novaConversa = new Conversa();
        novaConversa.setWaId(waid);
        novaConversa.setPhoneNumberId(phoneNumberId);
        novaConversa.setWabaId(wabaId);
        novaConversa.setCreatedAt(Instant.now());
        novaConversa.setLastMessageAt(Instant.now());
        // Salvar a nova conversa no banco de dados
        conversaRepository.save(novaConversa);

        String enviado = whatsAppGateway.enviarMensagemTemplate(waid);
        JsonNode payload = objectMapper.readTree(enviado);

        JsonNode messages = payload.path("messages");
        for (JsonNode message : messages) {
            String idMeta = message.path("id").stringValue();

            var mensagem = new Mensagem();
            mensagem.setIdMensagem(idMeta);
            mensagem.setConversa(novaConversa);
            mensagem.setDirecaoMensagem(DirecaoMensagem.SAIDA);
            mensagem.setTipoMensagem(TipoMensagem.TEXTO);
            mensagem.setBody("Template enviado pela Meta, aguarde a resposta do destinatário.");
            mensagem.setEnviadaEm(Instant.now());
            mensagem.setStatus(StatusMensagem.ENVIADA);
            mensagem.setProcessada(false);
            mensagemRepository.save(mensagem);
        }

        return response = "Nova conversa criada e template enviado com sucesso.";
    }

    // Metodo para criar conversa e mensagem a partir do payload recebido
    public void criarConversa(ConversaRequestDto request) {

        var conversaExistente = conversaRepository.findByWaIdAndPhoneNumberId(request.getWaId(),
                request.getPhoneNumberId());

        // Conversa ja existe para waId e phoneNumberId
        if (conversaExistente != null) {

            var mensagem = new Mensagem();
            mensagem.setIdMensagem(request.getIdMensagem());
            mensagem.setConversa(conversaExistente);
            mensagem.setBody(request.getBodyText());
            mensagem.setDirecaoMensagem(DirecaoMensagem.ENTRADA);
            mensagem.setStatus(StatusMensagem.RECEBIDA);
            mensagem.setRecebidaEm(Instant.now());
            mensagem.setProcessada(false);

            // Definir o tipo da mensagem com base no request
            String tipoMensagem = request.getTypeMessage();
            switch (tipoMensagem) {
                case "text":
                    mensagem.setTipoMensagem(TipoMensagem.TEXTO);
                    break;
                case "image":
                    mensagem.setTipoMensagem(TipoMensagem.IMAGEM);
                    break;
                case "video":
                    mensagem.setTipoMensagem(TipoMensagem.VIDEO);
                    break;
                case "audio":
                    mensagem.setTipoMensagem(TipoMensagem.AUDIO);
                    break;
                case "document":
                    mensagem.setTipoMensagem(TipoMensagem.DOCUMENTO);
                    break;
                default:
                    break;
            }

            // Salvar a mensagem de texto associada à conversa
            mensagemRepository.save(mensagem);

            conversaExistente.setLastMessageAt(request.getLastMessageAt());
            conversaExistente.setLastMessageId(request.getLastMessageId());
            // Salvar a nova conversa no banco de dados
            conversaRepository.save(conversaExistente);

            return;
        }

        // Conversa nao existe para waId e phoneNumberId
        var novaConversa = new Conversa();
        novaConversa.setWaId(request.getWaId());
        novaConversa.setPhoneNumberId(request.getPhoneNumberId());
        novaConversa.setDisplayPhoneNumber(request.getDisplayPhoneNumber());
        novaConversa.setLastMessageAt(request.getLastMessageAt());
        novaConversa.setLastMessageId(request.getLastMessageId());
        novaConversa.setWabaId(request.getWabaId());
        novaConversa.setCreatedAt(Instant.now());
        novaConversa.setLastMessageAt(request.getLastMessageAt());
        novaConversa.setLastMessageId(request.getLastMessageId());

        // Salvar a nova conversa no banco de dados
        conversaRepository.save(novaConversa);

        var mensagem = new Mensagem();
        mensagem.setIdMensagem(request.getIdMensagem());
        mensagem.setConversa(novaConversa);
        mensagem.setBody(request.getBodyText());
        mensagem.setDirecaoMensagem(DirecaoMensagem.ENTRADA);
        mensagem.setStatus(StatusMensagem.RECEBIDA);
        mensagem.setRecebidaEm(Instant.now());
        mensagem.setProcessada(false);

        // Definir o tipo da mensagem com base no request
        String tipoMensagem = request.getTypeMessage();
        switch (tipoMensagem) {
            case "text":
                mensagem.setTipoMensagem(TipoMensagem.TEXTO);
                break;
            case "image":
                mensagem.setTipoMensagem(TipoMensagem.IMAGEM);
                break;
            case "video":
                mensagem.setTipoMensagem(TipoMensagem.VIDEO);
                break;
            case "audio":
                mensagem.setTipoMensagem(TipoMensagem.AUDIO);
                break;
            case "document":
                mensagem.setTipoMensagem(TipoMensagem.DOCUMENTO);
                break;
            default:
                break;
        }

        // Salvar a mensagem de texto associada à conversa
        mensagemRepository.save(mensagem);

        return;
    }
}
