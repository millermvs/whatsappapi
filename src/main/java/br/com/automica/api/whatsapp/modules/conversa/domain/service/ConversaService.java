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
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.ConversaRepository;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.MensagemRepository;
import tools.jackson.databind.JsonNode;

@Service
public class ConversaService {

    @Autowired
    private ConversaRepository conversaRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    public String filtrar(JsonNode payload) {

        JsonNode entry0 = payload.path("entry").path(0);
        JsonNode change0 = entry0.path("changes").path(0);
        JsonNode value = change0.path("value");

        JsonNode metadata = value.path("metadata");
        JsonNode contact0 = value.path("contacts").path(0);
        JsonNode message0 = value.path("messages").path(0);

        // Se contem waId sera mensagem de conversa
        String waId = contact0.path("wa_id").stringValue(null);
        if (waId != null) {
            String phoneNumberId = metadata.path("phone_number_id").stringValue(null);
            String displayPhoneNumber = metadata.path("display_phone_number").stringValue(null);
            String messageId = message0.path("id").stringValue(null);
            String wabaId = entry0.path("id").stringValue(null);
            String bodyText = message0.path("text").path("body").stringValue(null);
            String typeMessage = message0.path("type").stringValue(null);

            // timestamp vem como "string numérica"
            long messageTimestamp = Long.parseLong(message0.path("timestamp").stringValue(null));

            var conversaRequestDto = new ConversaRequestDto();
            conversaRequestDto.setWaId(waId);
            conversaRequestDto.setPhoneNumberId(phoneNumberId);
            conversaRequestDto.setDisplayPhoneNumber(displayPhoneNumber);
            conversaRequestDto.setLastMessageAt(Instant.ofEpochSecond(messageTimestamp));
            conversaRequestDto.setLastMessageId(messageId);
            conversaRequestDto.setWabaId(wabaId);
            conversaRequestDto.setTypeMessage(typeMessage);
            conversaRequestDto.setBodyText(bodyText);
            conversaRequestDto.setIdMensagem(messageId);
            criarConversa(conversaRequestDto);
        } else {
            System.out.println("Não é uma mensagem de conversa válida.");
            return "Não é uma mensagem de conversa válida.";
        }
        return "Filtrado com sucesso!";
    }

    public void criarConversa(ConversaRequestDto request) {

        var conversaExistente = conversaRepository.findByWaIdAndPhoneNumberId(request.getWaId(),
                request.getPhoneNumberId());
        if (conversaExistente != null) {

            var mensagem = new Mensagem();
            mensagem.setIdMensagem(request.getIdMensagem());
            mensagem.setConversa(conversaExistente);
            mensagem.setBody(request.getBodyText());
            mensagem.setDirecaoMensagem(DirecaoMensagem.ENTRADA);
            mensagem.setStatus(StatusMensagem.RECEBIDA);

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

            System.out.println("Conversa já existe para waId=" + request.getWaId() + " e phoneNumberId="
                    + request.getPhoneNumberId());
            return;
        }

        var novaConversa = new Conversa();
        novaConversa.setWaId(request.getWaId());
        novaConversa.setPhoneNumberId(request.getPhoneNumberId());
        novaConversa.setDisplayPhoneNumber(request.getDisplayPhoneNumber());
        novaConversa.setLastMessageAt(request.getLastMessageAt());
        novaConversa.setLastMessageId(request.getLastMessageId());
        novaConversa.setWabaId(request.getWabaId());
        novaConversa.setCreatedAt(Instant.now());
        // Salvar a nova conversa no banco de dados
        conversaRepository.save(novaConversa);

        var mensagem = new Mensagem();
        mensagem.setIdMensagem(request.getIdMensagem());
        mensagem.setConversa(novaConversa);
        mensagem.setBody(request.getBodyText());
        mensagem.setDirecaoMensagem(DirecaoMensagem.ENTRADA);
        mensagem.setStatus(StatusMensagem.RECEBIDA);

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
