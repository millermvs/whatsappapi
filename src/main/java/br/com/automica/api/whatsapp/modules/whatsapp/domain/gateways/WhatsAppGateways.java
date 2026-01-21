package br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.MensagemOut;

public interface WhatsAppGateways {
    String enviarMensagemTexto(MensagemOut mensagem);

    String enviarMensagemTemplate(String destinatario);
}
