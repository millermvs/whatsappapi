package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtexto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaMensagemDeTextoRequest {	
	private String messaging_product;
	private String recipient_type;
	private String to;
	private String type;
	private TextAninhado text;	
}
