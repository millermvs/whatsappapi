package br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversaRequestDto {

    private String waId; // Identificação do cliente no WhatsApp (Remetente (Ex: 5521965250051))

    private String phoneNumberId; // Identificação estável do número WhatsApp Business (destinatário).

    private String displayPhoneNumber; // Número formatado para debug/log. NÃO entra em regra de negócio nem em chave

    private Instant lastMessageAt; // Data/hora da última mensagem recebida ou enviada. Ordenar a lista de conversas

    private String lastMessageId; // ID da última mensagem processada. Útil para debug, auditoria ou correlação futura

    private String wabaId; // Identificação do cliente no WhatsApp Business

    private String idMensagem; // ID da mensagem recebida ou enviada

    private String typeMessage; // Tipo da mensagem recebida (text, image, etc)

    private String bodyText; // Texto da mensagem recebida ou enviada

}
