package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.services.CaixaEntradaWebhookMetaService;

@Component
public class SchedulingComponent {

	@Autowired
	private CaixaEntradaWebhookMetaService caixaEntradaWebhookMetaService;
	
	@Scheduled(fixedDelay = 150000000)
    public void processarPendentes() {
		System.out.println("⏰ Scheduler rodando...");
        caixaEntradaWebhookMetaService.buscarMensagemNaoProcessadas();
    }
}
