package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.components;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways.ConversaGateway;
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
	private ConversaGateway conversaGateway;

	@Scheduled(fixedDelay = 15000) // Executa a cada 15 segundos
	public void processarPendentes() {
		System.out.println("Scheduler rodando...");
		var mensagensPendentes = caixaEntradaWebhookMetaRepository.findByProcessedFalse();

		if (mensagensPendentes != null && !mensagensPendentes.isEmpty()) {
			System.out.println(" Mensagens pendentes encontradas: " + mensagensPendentes.size());
			for (var mensagem : mensagensPendentes) {
				JsonNode payload = objectMapper.readTree(mensagem.getPayload());
				conversaGateway.filtrar(payload);
				mensagem.setProcessed(true);
				mensagem.setProcessedAt(Instant.now());
				caixaEntradaWebhookMetaRepository.save(mensagem);
			}

		} else {
			System.out.println("Nenhuma mensagem pendente.");
		}
	}
}
