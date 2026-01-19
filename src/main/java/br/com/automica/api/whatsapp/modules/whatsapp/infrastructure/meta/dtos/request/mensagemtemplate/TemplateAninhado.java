package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtemplate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateAninhado {
    private String name;
	private LanguageAninhado language;
}
