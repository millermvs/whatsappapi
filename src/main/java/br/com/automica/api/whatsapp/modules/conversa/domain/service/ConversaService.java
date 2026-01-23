package br.com.automica.api.whatsapp.modules.conversa.domain.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.automica.api.whatsapp.modules.conversa.domain.dtos.request.conversa.ConversaRequestDto;
import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Conversa;
import br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories.ConversaRepository;

@Service
public class ConversaService {

    @Autowired
    private ConversaRepository conversaRepository;

    ConversaService(ConversaRepository conversaRepository) {
        this.conversaRepository = conversaRepository;
    }

    public String criarConversa(ConversaRequestDto request) {
        var novaConversa = new Conversa();
        novaConversa.setWaId(request.getWaId());
        novaConversa.setPhoneNumberId(request.getPhoneNumberId());
        novaConversa.setDisplayPhoneNumber(request.getDisplayPhoneNumber());
        novaConversa.setLastMessageAt(request.getLastMessageAt());
        novaConversa.setLastMessageId(request.getLastMessageId());
        novaConversa.setWabaId(request.getWabaId());
        novaConversa.setCreatedAt(Instant.now());
        // Salvar a nova conversa no banco de dados
        conversaRepository.save(novaConversa);
        return "Conversa criada com sucesso!";
    }
}
