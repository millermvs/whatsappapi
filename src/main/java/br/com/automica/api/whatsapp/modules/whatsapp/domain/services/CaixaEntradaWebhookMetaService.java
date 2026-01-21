package br.com.automica.api.whatsapp.modules.whatsapp.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.entities.CaixaEntradaWebhookMeta;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.repositories.CaixaEntradaWebhookMetaRepository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class CaixaEntradaWebhookMetaService {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CaixaEntradaWebhookMetaRepository caixaEntradaWebhookMetaRepository;

	public void savePayload(JsonNode payload) {

		// Navegação no JSON
		JsonNode entry0 = payload.path("entry").path(0);
		JsonNode change0 = entry0.path("changes").path(0);
		JsonNode value = change0.path("value");

		// Campos simples
		String eventObject = payload.path("object").stringValue(null);
		String eventField = change0.path("field").stringValue(null);
		String wabaId = entry0.path("id").stringValue(null);

		JsonNode metadata = value.path("metadata");
		String phoneNumberId = metadata.path("phone_number_id").stringValue(null);
		String displayPhoneNumber = metadata.path("display_phone_number").stringValue(null);

		String waId = value.path("contacts").path(0).path("wa_id").stringValue(null);

		// Mensagem inbound (se existir)
		JsonNode message0 = value.path("messages").path(0);
		String messageId = message0.path("id").stringValue(null);

		Long messageTimestamp = message0.path("timestamp").asLongOpt().isPresent()
				? message0.path("timestamp").asLongOpt().getAsLong()
				: null;

		CaixaEntradaWebhookMeta novoPayload = new CaixaEntradaWebhookMeta();
		novoPayload.setEventObject(eventObject);
		novoPayload.setEventField(eventField);
		novoPayload.setWabaId(wabaId);
		novoPayload.setPhoneNumberId(phoneNumberId);
		novoPayload.setDisplayPhoneNumber(displayPhoneNumber);
		novoPayload.setWaId(waId);
		novoPayload.setMessageId(messageId);
		novoPayload.setMessageTimestamp(messageTimestamp);

		// JSON cru -> texto
		try {
			novoPayload.setPayload(objectMapper.writeValueAsString(payload));
		} catch (JacksonException e) {
			throw new IllegalStateException("Erro ao serializar payload", e);
		}

		// Salva. Se duplicar (unique message_id), ignora.
		try {
			caixaEntradaWebhookMetaRepository.save(novoPayload);
			// (se a exception está estourando fora do save):
			// caixaEntradaWebhookMetaRepository.flush();
		} catch (DataIntegrityViolationException ex) {
			// Duplicado por UNIQUE(message_id): não é erro para webhook, apenas ignore
			return;
		}
	}

}
