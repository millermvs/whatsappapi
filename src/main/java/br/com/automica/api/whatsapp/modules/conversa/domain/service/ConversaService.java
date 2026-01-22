package br.com.automica.api.whatsapp.modules.conversa.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;
import br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.repositories.CaixaEntradaWebhookMetaRepository;

@Service
public class ConversaService {

    @Autowired
    CaixaEntradaWebhookMetaRepository caixaEntradaWebhookMetaRepository;

    public String criarConversa(ConversaRequestDto request) {

        
        
        return "Conversa criada com sucesso!";
    }

}
