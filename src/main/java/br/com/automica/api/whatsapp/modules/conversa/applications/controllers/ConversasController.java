package br.com.automica.api.whatsapp.modules.conversa.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.automica.api.whatsapp.modules.conversa.domain.service.ConversaService;

@RestController
@RequestMapping("/conversas")
public class ConversasController {

    @Autowired
    private ConversaService conversaService;

    @PostMapping("criar")
    public ResponseEntity<String> postCriarConversa(@RequestParam String waid, @RequestParam String phoneNumberId,
            @RequestParam String wabaId) {
        String response = conversaService.criarConversaSistema(waid, phoneNumberId, wabaId);
        return ResponseEntity.ok(response);
    }
}
