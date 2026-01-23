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

	@Scheduled(fixedDelay = 15000)
	public void processarPendentes() {
		System.out.println("Scheduler rodando...");
		var mensagensPendentes = caixaEntradaWebhookMetaRepository.findByProcessedFalse();

		if (mensagensPendentes != null && !mensagensPendentes.isEmpty()) {
			System.out.println(" Mensagens pendentes encontradas: " + mensagensPendentes.size());
			for (var mensagem : mensagensPendentes) {

				JsonNode payload = objectMapper.readTree(mensagem.getPayload());

				JsonNode entry0 = payload.path("entry").path(0);
				JsonNode change0 = entry0.path("changes").path(0);
				JsonNode value = change0.path("value");

				JsonNode metadata = value.path("metadata");
				JsonNode contact0 = value.path("contacts").path(0);
				JsonNode message0 = value.path("messages").path(0);

				// Strings direto em variáveis
				String waId = contact0.path("wa_id").stringValue(null);
				if (waId == null) {
					System.out.println("  waId é nulo, pulando mensagem id=" + mensagem.getMessageId());
					continue;
				}
				String phoneNumberId = metadata.path("phone_number_id").stringValue(null);
				String displayPhoneNumber = metadata.path("display_phone_number").stringValue(null);
				String messageId = message0.path("id").stringValue(null);
				String wabaId = entry0.path("id").stringValue(null);;

				// timestamp vem como "string numérica"
				long messageTimestamp = Long.parseLong(message0.path("timestamp").stringValue(null));

				var conversaRequestDto = new ConversaRequestDto();
				conversaRequestDto.setWaId(waId);
				conversaRequestDto.setPhoneNumberId(phoneNumberId);
				conversaRequestDto.setDisplayPhoneNumber(displayPhoneNumber);
				conversaRequestDto.setLastMessageAt(Instant.ofEpochSecond(messageTimestamp));
				conversaRequestDto.setLastMessageId(messageId);
				conversaRequestDto.setWabaId(wabaId);

				// fazer o que precisa com o DTO
				// conversaService.criarOuAtualizar(conversaRequestDto);

				System.out.println(" DTO: waId=" + waId
						+ " phoneNumberId=" + phoneNumberId
						+ " msgId=" + messageId
						+ " lastMessageAt=" + Instant.ofEpochSecond(messageTimestamp));

				conversaGateway.criarConversa(conversaRequestDto);

				//TODO:
				// marque como processada se der tudo certo
				// mensagem.setProcessed(true);
				// mensagem.setProcessedAt(Instant.now());
				// caixaEntradaWebhookMetaRepository.save(mensagem);

			}

		} else {
			System.out.println("Nenhuma mensagem pendente.");
		}
	}
}
