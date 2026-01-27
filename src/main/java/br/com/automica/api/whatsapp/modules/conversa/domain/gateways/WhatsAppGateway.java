package br.com.automica.api.whatsapp.modules.conversa.domain.gateways;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.mensagem.request.MensagemRequestDto;

public interface WhatsAppGateway {
    String enviarMensagem(MensagemRequestDto mensagem);

    String enviarMensagemTemplate(String destinatario);
}
