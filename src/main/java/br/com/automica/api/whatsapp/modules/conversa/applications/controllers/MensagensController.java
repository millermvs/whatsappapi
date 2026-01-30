package br.com.automica.api.whatsapp.modules.conversa.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.mensagem.request.MensagemRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.response.MensagensDaConversaResponseDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.service.MensagemService;

@RestController
@RequestMapping("/mensagens")
public class MensagensController {

	@Autowired
	private MensagemService mensagemService;

	@PostMapping("enviar/texto")
	public ResponseEntity<String> post(@RequestBody MensagemRequestDto request) {
		var response = mensagemService.enviarMensagemTexto(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<MensagensDaConversaResponseDto>> getMensagensDaConversa(@RequestParam Long idconversa,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "30") Integer size
		) {
			var response = mensagemService.listarMensagensDaConversa(idconversa, page, size);
		return ResponseEntity.ok(response);
	}
}
