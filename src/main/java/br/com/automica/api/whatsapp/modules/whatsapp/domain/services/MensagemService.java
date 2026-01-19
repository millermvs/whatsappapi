package br.com.automica.api.whatsapp.modules.whatsapp.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.dtos.request.mensagem.MensagemRequestDto;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.gateways.WhatsAppGateways;
import br.com.automica.api.whatsapp.modules.whatsapp.domain.models.Mensagem;

@Service
public class MensagemService {
	
	@Autowired
	private WhatsAppGateways whatsAppGateway;
	
	public String enviarMensagem(MensagemRequestDto request) {
		
		var mensagem = new Mensagem();
		mensagem.setDestinatario(request.getDestinatario());
		mensagem.setTipo(request.getTipo());
		mensagem.setConteudo(request.getConteudo());
		mensagem.setPreviewUrl(false);
		
		var response = whatsAppGateway.enviarMensagemTexto(mensagem);
		
		return response;		
	}
	
public String enviarTemplate(String destinatario) {		
				
		var response = whatsAppGateway.enviarMensagemTemplate(destinatario);
		
		return response;		
	}
}
