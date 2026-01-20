package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtemplate.MetaMensagemTemplateRequestDto;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.meta.dtos.request.mensagemtexto.MetaMensagemDeTextoRequest;

@Component
public class MetaWhatsAppClient {

	private final WebClient webClient;
	
	@Value("${meta.token}")
	private String token;

	public MetaWhatsAppClient(WebClient webCliente) {
		this.webClient = webCliente;
	}

	public String enviarMensagemTexto(MetaMensagemDeTextoRequest mensagem) {
		return webClient.post().uri("v22.0/445562525305334/messages")
				.header("Authorization", "Bearer " + this.token).bodyValue(mensagem).retrieve().bodyToMono(String.class)
				.block();
	}
	
	public String enviarMensagemTemplate(MetaMensagemTemplateRequestDto mensagem) {
		return webClient.post().uri("v22.0/445562525305334/messages")
				.header("Authorization", "Bearer " + this.token).bodyValue(mensagem).retrieve().bodyToMono(String.class)
				.block();
	}
}
