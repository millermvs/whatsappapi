package br.com.automica.api.whatsapp.modules.conversa.domain.adpter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.service.ConversaService;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways.ConversaGateway;

@Component
public class ConversaAdapter implements ConversaGateway {

    @Autowired
    private ConversaService conversaService;

    @Override
    public void criarConversa(ConversaRequestDto request) {
        conversaService.criarConversa(request);
        return;
    }
}
