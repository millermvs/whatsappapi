package br.com.automica.api.whatsapp.modules.whatsapp.applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.services.CaixaEntradaWebhookMetaService;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/webhook")
public class WhatsAppWebhookController {

	@Value("${meta.webhook.verify-token}")
	private String expectedVerifyToken;

	@Autowired
	private CaixaEntradaWebhookMetaService caixaEntradaWebhookMeta;

	@GetMapping
	public ResponseEntity<String> verificarWebhook(@RequestParam(name = "hub.mode", required = false) String mode,
			@RequestParam(name = "hub.verify_token", required = false) String verifyToken,
			@RequestParam(name = "hub.challenge", required = false) String challenge) {

		boolean isSubscribe = "subscribe".equals(mode);
		boolean tokenOk = expectedVerifyToken != null && expectedVerifyToken.equals(verifyToken);

		if (isSubscribe && tokenOk) {
			return ResponseEntity.ok(challenge);
		}

		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@PostMapping
	public ResponseEntity<Void> receberWebhook(@RequestBody JsonNode payload) {

		caixaEntradaWebhookMeta.savePayload(payload);
		
		//System.out.println("WEBHOOK RECEBIDO: " + payload);

		return ResponseEntity.ok().build();
	}
}
