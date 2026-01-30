package br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Conversa;

@Repository
public interface ConversaRepository extends JpaRepository<Conversa, Long> {

    @Query("""
            SELECT c FROM Conversa c
            WHERE c.waId = :waId AND c.phoneNumberId = :phoneNumberId
                """)
    Conversa findByWaIdAndPhoneNumberId(String waId, String phoneNumberId);

    Page<Conversa> findAllBy(Pageable pageable);

}
