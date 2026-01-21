package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.adapter;

import org.springframework.stereotype.Component;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways.WhatsAppGateways;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.MensagemOut;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.client.MetaWhatsAppClient;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.mappers.MetaMensagemMapper;

@Component
public class WhatsAppGatewayMetaAdapter implements WhatsAppGateways {

	private final MetaWhatsAppClient metaWhatsAppClient;
	private final MetaMensagemMapper metaMensagemMapper;

	public WhatsAppGatewayMetaAdapter(MetaWhatsAppClient metaWhatsAppClient, MetaMensagemMapper metaMensagemMapper) {
		this.metaWhatsAppClient = metaWhatsAppClient;
		this.metaMensagemMapper = metaMensagemMapper;
	}

	@Override
	public String enviarMensagemTexto(MensagemOut mensagem) {
		var request = metaMensagemMapper.transformarMetaMensagemDeTexto(mensagem);
		var response = metaWhatsAppClient.enviarMensagemTexto(request);
		return response;
	}

	@Override
	public String enviarMensagemTemplate(String destinatario) {
		var request = metaMensagemMapper.transformarMetaMensagemTemplate(destinatario);
		var response = metaWhatsAppClient.enviarMensagemTemplate(request);
		return response;
	}
}
