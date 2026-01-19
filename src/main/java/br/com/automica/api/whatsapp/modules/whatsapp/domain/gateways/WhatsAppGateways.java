package br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.Mensagem;

public interface WhatsAppGateways {
    String enviarMensagemTexto(Mensagem mensagem);
	String enviarMensagemTemplate(String destinatario);
}
