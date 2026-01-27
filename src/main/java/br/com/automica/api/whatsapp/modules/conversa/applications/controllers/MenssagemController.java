package br.com.automica.api.whatsapp.modules.conversa.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.mensagem.request.MensagemRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.service.MensagemService;

@RestController
@RequestMapping("/mensagens/enviar")
public class MenssagemController {

	@Autowired
	private MensagemService mensagemService;

	@PostMapping("texto")
	public ResponseEntity<String> post(@RequestBody MensagemRequestDto request) {
		mensagemService.enviarMensagemTexto(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("template")
	public ResponseEntity<String> postTemplate(@RequestParam String destinatario) {
		var response = mensagemService.enviarTemplate(destinatario);
		return ResponseEntity.ok(response);
	}

}
