package br.com.automica.api.whatsapp.modules.whatsapp.domain.adpters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.mensagem.request.MensagemRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.gateways.WhatsAppGateway;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.MensagemOut;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.services.CaixaSaidaWebhookMetaService;

@Component
public class WhatsAppGatewayMetaAdapter implements WhatsAppGateway {

	@Autowired
	private CaixaSaidaWebhookMetaService caixaSaidaWebhookMetaService;

	@Override
	public String enviarMensagem(MensagemRequestDto mensagemRequest) {
		var mensagem = new MensagemOut();
		mensagem.setDestinatario(mensagemRequest.getDestinatario());
		mensagem.setTipo(mensagemRequest.getTipo());
		mensagem.setBody(mensagemRequest.getBody());
		mensagem.setPreviewUrl(false);

		caixaSaidaWebhookMetaService.enviarMensagem(mensagem);

		return "Mensagem enviada via Meta";
	}

	@Override
	public String enviarMensagemTemplate(String destinatario) {
		caixaSaidaWebhookMetaService.enviarTemplate(destinatario);
		return "Template enviado via Meta";
	}
}
