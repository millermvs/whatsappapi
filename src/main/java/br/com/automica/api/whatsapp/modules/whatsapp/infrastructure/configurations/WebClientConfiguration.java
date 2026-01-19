package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfiguration {

    @Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl("https://graph.facebook.com")
				.build();		
	}

}