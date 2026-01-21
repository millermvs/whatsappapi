package br.com.automica.api.whatsapp.modules.whatsapp.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.automica.api.whatsapp.modules.whatsapp.domain.entities.CaixaEntradaWebhookMeta;

@Repository
public interface CaixaEntradaWebhookMetaRepository extends JpaRepository<CaixaEntradaWebhookMeta, UUID> {
    @Query("""
			SELECT m FROM CaixaEntradaWebhookMeta m
			WHERE m.processed = false
			ORDER BY m.messageTimestamp
			""")
	List<CaixaEntradaWebhookMeta> findByProcessedFalse();

	boolean existsByMessageId(String messageId);

}
