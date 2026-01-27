package br.com.automica.api.whatsapp.modules.whatsapp.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.MensagemOut;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.client.MetaWhatsAppClient;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.mappers.MetaMensagemMapper;

@Service
public class CaixaSaidaWebhookMetaService {

	@Autowired
	private MetaMensagemMapper metaMensagemMapper;

	@Autowired
	private MetaWhatsAppClient metaWhatsAppClient;

	public String enviarMensagem(MensagemOut mensagem) {

		var request = metaMensagemMapper.transformarMetaMensagemDeTexto(mensagem);
		var response = metaWhatsAppClient.enviarMensagemTexto(request);

		return response;
	}

	public String enviarTemplate(String destinatario) {

		var request = metaMensagemMapper.transformarMetaMensagemTemplate(destinatario);
		var response = metaWhatsAppClient.enviarMensagemTemplate(request);

		return response;
	}
}