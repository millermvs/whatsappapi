package br.com.automica.api.whatsapp.modules.shared.components;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.automica.api.whatsapp.modules.conversa.domain.gateways.PayloadGateway;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.repositories.CaixaEntradaWebhookMetaRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class SchedulingComponent {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CaixaEntradaWebhookMetaRepository caixaEntradaWebhookMetaRepository;

	@Autowired
	private PayloadGateway filtrarPayload;

	@Scheduled(fixedDelay = 15000) // Executa a cada 15 segundos
	public void processarPendentes() {
		System.out.println("Scheduler rodando...");
		var mensagensPendentes = caixaEntradaWebhookMetaRepository.findByProcessedFalse();

		if (mensagensPendentes != null && !mensagensPendentes.isEmpty()) {
			for (var mensagem : mensagensPendentes) {
				JsonNode payload = objectMapper.readTree(mensagem.getPayload());
				filtrarPayload.filtrarPayload(payload);
				mensagem.setProcessed(true);
				mensagem.setProcessedAt(Instant.now());
				caixaEntradaWebhookMetaRepository.save(mensagem);
			}

		} else {
			System.out.println("Nenhuma mensagem pendente.");
		}
	}
}
