package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.mappers;

import org.springframework.stereotype.Component;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.MensagemOut;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtemplate.LanguageAninhado;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtemplate.MetaMensagemTemplateRequestDto;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtemplate.TemplateAninhado;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtexto.MetaMensagemDeTextoRequest;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtexto.TextAninhado;

@Component
public class MetaMensagemMapper {

	public MetaMensagemDeTextoRequest transformarMetaMensagemDeTexto(MensagemOut mensagem) {
		var text = new TextAninhado();
		text.setPreview_url(mensagem.getPreviewUrl());
		text.setBody(mensagem.getBody());

		var response = new MetaMensagemDeTextoRequest();
		response.setMessaging_product("whatsapp");
		response.setRecipient_type("individual");
		response.setTo(mensagem.getDestinatario());
		response.setType(mensagem.getTipo());
		response.setText(text);
		return response;
	}

	public MetaMensagemTemplateRequestDto transformarMetaMensagemTemplate(String destinatario) {
		var language = new LanguageAninhado();
		language.setCode("pt_Br");

		var template = new TemplateAninhado();
		template.setName("01_conversa_informacoes");
		template.setLanguage(language);

		var response = new MetaMensagemTemplateRequestDto();
		response.setMessaging_product("whatsapp");
		response.setRecipient_type("individual");
		response.setTo(destinatario);
		response.setType("template");
		response.setTemplate(template);
		return response;
	}
}
