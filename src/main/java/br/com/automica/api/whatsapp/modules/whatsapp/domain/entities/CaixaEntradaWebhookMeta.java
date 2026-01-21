package br.com.automica.api.whatsapp.modules.whatsapp.domain.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "meta_webhook_inbox", indexes = { @Index(name = "idx_inbox_processed", columnList = "processed"),
		@Index(name = "idx_inbox_received_at", columnList = "receivedAt"),
		@Index(name = "idx_inbox_phone_number_id", columnList = "phoneNumberId"),
		@Index(name = "idx_inbox_wa_id", columnList = "waId") })
public class CaixaEntradaWebhookMeta {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private Instant receivedAt;

	@Column
	private String eventObject;

	@Column
	private String eventField;

	@Column
	private String wabaId;

	@Column
	private String phoneNumberId;

	@Column
	private String displayPhoneNumber;

	@Column
	private String waId;

	@Column(unique = true)
	private String messageId;

	@Column
	private Long messageTimestamp;

	@Column(nullable = false, columnDefinition = "text")
	private String payload;

	@Column(nullable = false)
	private Boolean processed;

	@Column
	private Instant processedAt;

	@Column(columnDefinition = "text")
	private String processingError;

	@PrePersist
	private void prePersist() {
		if (processed == null) {
			processed = false;
		}
		if (receivedAt == null) {
			receivedAt = Instant.now();
		}
	}

}
