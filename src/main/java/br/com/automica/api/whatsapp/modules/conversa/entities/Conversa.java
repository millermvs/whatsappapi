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
	        @UniqueConstraint(columnNames = {"phone_number_id", "wa_id"})
	    }
	)
public class Conversa {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String phoneNumberId; // Identificação do número de telefone
	
	@Column(nullable = false)
	private String waId; // Identificação do número de telefone remetente
	
	@Column
	private String wabaId; // Identificação da conta do WhatsApp Business

	@Column
	private String contactName;
	
	@Column
	private String lastMessagePreview;
	
	@Column
	private Instant lastMessageAt;
	
	@Column
	private Integer unreadCount;
	
}
