package br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.mensagem.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensagemRequestDto {

	private Long idConversa;

	private String destinatario;

	private String tipo;

	private String body;

}
