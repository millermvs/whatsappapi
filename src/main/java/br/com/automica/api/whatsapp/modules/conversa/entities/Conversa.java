package br.com.automica.api.whatsapp.modules.conversa.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "conversas",
    uniqueConstraints = {
        // Garante que só exista UMA conversa por cliente + número business
        @UniqueConstraint(columnNames = {"phone_number_id", "wa_id"})
    }
)
public class Conversa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    // Identificador técnico da tabela (não faz parte da identidade de negócio)
    private Long id;

    @Column(name = "wa_id", nullable = false)
    // Identificação do cliente no WhatsApp (remetente)
    // Ex: 5521965250053
    private String waId;

    @Column(name = "phone_number_id", nullable = false)
    // Identificação estável do número WhatsApp Business (destinatário)
    // Esse é o ID "de verdade" na Meta
    private String phoneNumberId;

    @Column(name = "display_phone_number")
    // Número formatado para debug/log
    // NÃO entra em regra de negócio nem em chave
    private String displayPhoneNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    // Momento em que a conversa foi criada pela primeira vez
    private Instant createdAt;

    @Column(name = "last_message_at")
    // Data/hora da última mensagem recebida ou enviada
    // Usado para ordenar a lista de conversas
    private Instant lastMessageAt;

    @Column(name = "last_message_id")
    // ID da última mensagem processada
    // Útil para debug, auditoria ou correlação futura
    private String lastMessageId;

}

