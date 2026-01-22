package br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Conversa;

@Repository
public interface ConversaRepository extends JpaRepository<Conversa, Long> {

}
