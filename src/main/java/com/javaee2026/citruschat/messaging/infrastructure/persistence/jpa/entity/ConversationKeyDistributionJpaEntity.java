package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = TableNames.Messaging.CONVERSATION_KEY_DISTRIBUTIONS, uniqueConstraints = @UniqueConstraint(name = "uk_conversation_device_version", columnNames = {
		"conversationId", "targetDeviceId", "keyVersion"}))
@Getter
@Setter
public class ConversationKeyDistributionJpaEntity {

	@Id
	private UUID id;

	@Column(nullable = false)
	private UUID conversationId;

	@Column(nullable = false)
	private UUID targetUserId;

	@Column(nullable = false)
	private UUID targetDeviceId;

	@Column(nullable = false)
	private Integer keyVersion;

	@Column(nullable = false, length = 4096)
	private String ciphertext;

	@Column(nullable = false, length = 512)
	private String iv;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Transient
	private boolean isNew = false;

	public void markNew() {
		this.isNew = true;
	}
}
