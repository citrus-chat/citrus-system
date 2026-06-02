package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "device_one_time_prekeys", indexes = {@Index(name = "idx_prekey_device", columnList = "device_id"),
		@Index(name = "idx_prekey_consumed", columnList = "consumed_at")})
@Getter
@NoArgsConstructor
public class DeviceOneTimePreKeyJpaEntity {

	@EmbeddedId
	private DeviceOneTimePreKeyJpaId id;

	@Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
	private String publicKey;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "consumed_at")
	private Instant consumedAt;

	public DeviceOneTimePreKeyJpaEntity( // El constructor del domain es privado
			DeviceOneTimePreKeyJpaId id, String publicKey, Instant createdAt, Instant consumedAt) {
		this.id = id;
		this.publicKey = publicKey;
		this.createdAt = createdAt;
		this.consumedAt = consumedAt;
	}
}
