package br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;

public interface ConversaGateway {
    void criarConversa(ConversaRequestDto conversaRequestDto); //faz a criação de uma conversa através da classe ConversaAdapter
}
