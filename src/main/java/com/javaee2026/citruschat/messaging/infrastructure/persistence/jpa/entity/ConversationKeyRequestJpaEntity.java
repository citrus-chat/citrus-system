package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "conversation_key_requests", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"conversation_id", "target_device_id"})}, indexes = {
				@Index(name = "idx_ckr_target_device", columnList = "target_device_id"),
				@Index(name = "idx_ckr_conversation", columnList = "conversation_id")})
@Getter
@Setter
@NoArgsConstructor
public class ConversationKeyRequestJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "conversation_id", nullable = false)
	private UUID conversationId;

	@Column(name = "target_user_id", nullable = false)
	private UUID targetUserId;

	@Column(name = "target_device_id", nullable = false)
	private UUID targetDeviceId;

	@Column(name = "target_public_key", nullable = false, columnDefinition = "TEXT")
	private String targetPublicKey;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
}
