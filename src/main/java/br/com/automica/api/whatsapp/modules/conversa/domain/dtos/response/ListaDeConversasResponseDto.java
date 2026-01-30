package br.com.automica.api.whatsapp.modules.conversa.domain.dtos.response;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListaDeConversasResponseDto {

    private String waId;

    private String wabaId;

    private String phoneNumberId;

    private String displayPhoneNumber;

    private Instant createdAt;

    private Instant lastMessageAt;

    private String lastMessageId;

    // private List<MesagensDaConversaResponseDto> mensagensTexto;

}
