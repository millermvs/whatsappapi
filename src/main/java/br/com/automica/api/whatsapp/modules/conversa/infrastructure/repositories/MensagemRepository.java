package br.com.automica.api.whatsapp.modules.conversa.infrastructure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.automica.api.whatsapp.modules.conversa.domain.entities.Mensagem;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, String> {

    @Query("""
            SELECT m FROM Mensagem m
            WHERE m.conversa.idConversa = :idconversa
            ORDER BY COALESCE(m.enviadaEm, m.recebidaEm) ASC
                """)
    Page<Mensagem> findByIdConversa(@Param("idconversa") Long idConversa, Pageable pageable);
}
