package br.com.automica.api.whatsapp.modules.whatsapp.domain.dtos.request.mensagem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensagemOutRequestDto {

	private String destinatario;

	private String tipo;

	private String conteudo;

}
