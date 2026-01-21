package br.com.automica.api.whatsapp.modules.whatsapp.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.dtos.request.mensagem.MensagemOutRequestDto;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways.WhatsAppGateways;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.MensagemOut;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.repositories.CaixaEntradaWebhookMetaRepository;

@Service
public class MensagemOutService {

	@Autowired
	private WhatsAppGateways whatsAppGateway;

	@Autowired
	private CaixaEntradaWebhookMetaRepository caixaEntradaWebhookMetaRepository;

	public String enviarMensagemTexto(MensagemOutRequestDto request) {

		var mensagem = new MensagemOut();
		mensagem.setDestinatario(request.getDestinatario());
		mensagem.setTipo(request.getTipo());
		mensagem.setConteudo(request.getConteudo());
		mensagem.setPreviewUrl(false);

		var response = whatsAppGateway.enviarMensagemTexto(mensagem);

		return response;
	}

	public String enviarTemplate(String destinatario) {

		var response = whatsAppGateway.enviarMensagemTemplate(destinatario);

		return response;
	}

	public MensagemOut processarMensagensTexto() {
		
		// System.out.println("🔎 Buscando mensagens não processadas...");

		var mensagensNaoProcessadas = caixaEntradaWebhookMetaRepository.findByProcessedFalse();
		// System.out.println("📦 Encontradas " + mensagensNaoProcessadas.size() + "
		// mensagens pendentes");

		List<MensagemOut> mensagensNovas = new ArrayList<MensagemOut>();

		if (mensagensNaoProcessadas.isEmpty()) {
			
		} else {
			for (var mensagens : mensagensNaoProcessadas) {
				System.out.println("➡️ Processando inbox ID: " + mensagens.getId());
				
			}
		}
		return null;
	}
}

/*
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

					String conteudo = objectMapper.writeValueAsString(message0.path("text").path("body"));

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
		*/
