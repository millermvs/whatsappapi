package br.com.automica.api.whatsapp.modules.whatsapp.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.dtos.request.mensagem.MensagemRequestDto;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.services.MensagemService;



@RestController
@RequestMapping("/whatsapp/mensagens")
public class MensagemController {

	@Autowired
	MensagemService mensagemService;

	@PostMapping("enviar")
	public ResponseEntity<String> post(@RequestBody MensagemRequestDto request) {
		var response = mensagemService.enviarMensagem(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("template")
	public ResponseEntity<String> postTemplate(@RequestParam String destinatario) {
		var response = mensagemService.enviarTemplate(destinatario);
		return ResponseEntity.ok(response);
	}
}