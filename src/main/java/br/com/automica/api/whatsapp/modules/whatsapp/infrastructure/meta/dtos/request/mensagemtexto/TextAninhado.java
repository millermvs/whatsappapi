package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtexto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextAninhado {	
	private Boolean preview_url;
	private String body;
}
