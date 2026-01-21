package br.com.automica.api.whatsapp.modules.whatsapp.domain.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.entities.CaixaEntradaWebhookMeta;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.Mensagem;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.repositories.CaixaEntradaWebhookMetaRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class CaixaEntradaWebhookMetaService {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CaixaEntradaWebhookMetaRepository caixaEntradaWebhookMetaRepository;

	@Transactional
	public List<Mensagem> buscarMensagemNaoProcessadas() {
		System.out.println("🔎 Buscando mensagens não processadas...");

		var mensagensNaoProcessadas = caixaEntradaWebhookMetaRepository.findByProcessedFalse();
		System.out.println("📦 Encontradas " + mensagensNaoProcessadas.size() + " mensagens pendentes");


		List<Mensagem> mensagensNovas = new ArrayList<Mensagem>();

		if (mensagensNaoProcessadas.isEmpty()) {
			return mensagensNovas;
		} else {
			for (var mensagens : mensagensNaoProcessadas) {
				System.out.println("➡️ Processando inbox ID: " + mensagens.getId());

				try {
					JsonNode payload = objectMapper.readTree(mensagens.getPayload());

					JsonNode entry0 = payload.path("entry").path(0);
					JsonNode change0 = entry0.path("changes").path(0);
					JsonNode value = change0.path("value");
					JsonNode message0 = value.path("messages").path(0);

					String conteudo = message0.path("text").path("body").asText();

					var mensagem = new Mensagem();
					mensagem.setDestinatario(mensagens.getPhoneNumberId());
					mensagem.setTipo("text");
					mensagem.setPreviewUrl(false);
					mensagem.setConteudo(conteudo);

					mensagensNovas.add(mensagem);
					mensagens.setProcessed(true);
					mensagens.setProcessedAt(Instant.now());
			        mensagens.setProcessingError(null);
			        
			        System.out.println("✅ Processado com sucesso | inbox ID: " + mensagens.getId());

					
				} catch (Exception e) {
					mensagens.setProcessingError(e.getMessage());
			        mensagens.setProcessed(false);
			        System.err.println("Erro ao processar inbox " + mensagens.getId() + ": " + e.getMessage());
				}				
			}
			return mensagensNovas;
		}

	}

	@Transactional
	public void savePayload(JsonNode payload) {		
		
		JsonNode entry0 = payload.path("entry").path(0);
		JsonNode change0 = entry0.path("changes").path(0);
		JsonNode value = change0.path("value");

		JsonNode metadata = value.path("metadata");
		JsonNode contact0 = value.path("contacts").path(0);
		JsonNode message0 = value.path("messages").path(0);

		CaixaEntradaWebhookMeta novoPayload = new CaixaEntradaWebhookMeta();
		novoPayload.setPayload(payload.toString());
		novoPayload.setEventObject(text(payload, "object"));
		novoPayload.setEventField(text(change0, "field"));
		novoPayload.setWabaId(text(entry0, "id"));

		novoPayload.setPhoneNumberId(text(metadata, "phone_number_id"));
		novoPayload.setDisplayPhoneNumber(text(metadata, "display_phone_number"));

		novoPayload.setWaId(text(contact0, "wa_id"));

		novoPayload.setMessageId(text(message0, "id"));
		novoPayload.setMessageTimestamp(longFromText(message0, "timestamp"));

		caixaEntradaWebhookMetaRepository.save(novoPayload);

	}

	private String text(JsonNode node, String field) {
		JsonNode v = node.path(field);
		if (v.isMissingNode() || v.isNull()) {
			return null;
		}
		String t = v.asText();
		return (t != null && t.isBlank()) ? null : t;
	}

	private Long longFromText(JsonNode node, String field) {
		String raw = text(node, field);
		if (raw == null) {
			return null;
		}
		try {
			return Long.parseLong(raw);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

