package br.com.automica.api.whatsapp.modules.conversa.domain.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Conversa;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.MensagemTexto;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.ConversaRepository;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.MensagemTextoRepository;
import tools.jackson.databind.JsonNode;

@Service
public class ConversaService {

    @Autowired
    private ConversaRepository conversaRepository;

    @Autowired
    private MensagemTextoRepository mensagemTextoRepository;

    public String filtrar(JsonNode payload) {

        JsonNode entry0 = payload.path("entry").path(0);
        JsonNode change0 = entry0.path("changes").path(0);
        JsonNode value = change0.path("value");

        JsonNode metadata = value.path("metadata");
        JsonNode contact0 = value.path("contacts").path(0);
        JsonNode message0 = value.path("messages").path(0);

        // Strings direto em variáveis
        String waId = contact0.path("wa_id").stringValue(null);
        if (waId == null) {            
            System.out.println("waId é nulo, ignorando payload.");
            return "waId nulo";
        }
        String phoneNumberId = metadata.path("phone_number_id").stringValue(null);
        String displayPhoneNumber = metadata.path("display_phone_number").stringValue(null);
        String messageId = message0.path("id").stringValue(null);
        String wabaId = entry0.path("id").stringValue(null);
        String bodyText = message0.path("text").path("body").stringValue(null);

        // timestamp vem como "string numérica"
        long messageTimestamp = Long.parseLong(message0.path("timestamp").stringValue(null));

        var conversaRequestDto = new ConversaRequestDto();
        conversaRequestDto.setWaId(waId);
        conversaRequestDto.setPhoneNumberId(phoneNumberId);
        conversaRequestDto.setDisplayPhoneNumber(displayPhoneNumber);
        conversaRequestDto.setLastMessageAt(Instant.ofEpochSecond(messageTimestamp));
        conversaRequestDto.setLastMessageId(messageId);
        conversaRequestDto.setWabaId(wabaId);
        conversaRequestDto.setBodyText(bodyText);
        conversaRequestDto.setIdMensagem(messageId);

        // fazer o que precisa com o DTO
        // conversaService.criarOuAtualizar(conversaRequestDto);

        System.out.println(" DTO: waId=" + waId
                + " phoneNumberId=" + phoneNumberId
                + " msgId=" + messageId
                + " lastMessageAt=" + Instant.ofEpochSecond(messageTimestamp));

                
        return "Filtrado com sucesso!";
    }

    public String criarConversa(ConversaRequestDto request) {

        var conversaExistente = conversaRepository.findByWaIdAndPhoneNumberId(request.getWaId(),
                request.getPhoneNumberId());
        if (conversaExistente != null) {

            var mensagemTexto = new MensagemTexto();
            mensagemTexto.setIdMensagem(request.getIdMensagem());
            mensagemTexto.setConversa(conversaExistente);
            mensagemTexto.setBody(request.getBodyText());
            // Salvar a mensagem de texto associada à conversa
            mensagemTextoRepository.save(mensagemTexto);

            System.out.println("Conversa já existe para waId=" + request.getWaId() + " e phoneNumberId="
                    + request.getPhoneNumberId());
            return "Conversa já existe!";
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

        var mensagemTexto = new MensagemTexto();
        mensagemTexto.setIdMensagem(request.getIdMensagem());
        mensagemTexto.setConversa(novaConversa);
        mensagemTexto.setBody(request.getBodyText());
        // Salvar a mensagem de texto associada à conversa
        mensagemTextoRepository.save(mensagemTexto);

        return "Conversa criada com sucesso!";
    }
}
