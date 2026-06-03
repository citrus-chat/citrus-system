package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "device_identities")
@Getter
@NoArgsConstructor
public class DeviceIdentityJpaEntity {
	@Id
	@Column(name = "device_id", nullable = false)
	private UUID deviceId;

	@Column(name = "public_identity_key", nullable = false, columnDefinition = "TEXT")
	private String publicIdentityKey;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	public DeviceIdentityJpaEntity(UUID deviceId, String publicIdentityKey, Instant createdAt) {
		this.deviceId = deviceId;
		this.publicIdentityKey = publicIdentityKey;
		this.createdAt = createdAt;
	}
}
