package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "device_signed_prekeys", indexes = {@Index(name = "idx_signed_prekey_device", columnList = "device_id"),
		@Index(name = "idx_signed_prekey_expires", columnList = "expires_at")})
@Getter
@NoArgsConstructor
public class DeviceSignedPreKeyJpaEntity {
	@EmbeddedId
	private DeviceSignedPreKeyJpaId id;

	@Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
	private String publicKey;

	@Column(name = "signature", nullable = false, columnDefinition = "TEXT")
	private String signature;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "expires_at")
	private Instant expiresAt;

	public DeviceSignedPreKeyJpaEntity(DeviceSignedPreKeyJpaId id, String publicKey, String signature,
			Instant createdAt, Instant expiresAt) {
		this.id = id;
		this.publicKey = publicKey;
		this.signature = signature;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}
}
