package br.com.automica.api.whatsapp.modules.conversa.applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/conversas")
public class ConversasController {

    @PostMapping("criar")
    public ResponseEntity<?> postCriarConversa(@RequestBody ConversaRequestDto request) {
        var response = "Em construção!";
        return ResponseEntity.ok(response);
    }
}
