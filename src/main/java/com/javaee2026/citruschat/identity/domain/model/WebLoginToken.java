package com.javaee2026.citruschat.identity.domain.model;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class WebLoginToken {

	private final UUID id;
	private final String tokenHash;
	private final UUID webDeviceId;
	private final String webDeviceName;
	private final String webPublicKey;
	private WebLoginTokenStatus status;
	private final Instant expiresAt;
	private Instant usedAt;
	private final Instant createdAt;
	private Instant updatedAt;

	private WebLoginToken(UUID id, String tokenHash, UUID webDeviceId, String webDeviceName, String webPublicKey,
			WebLoginTokenStatus status, Instant expiresAt, Instant usedAt, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.tokenHash = tokenHash;
		this.webDeviceId = webDeviceId;
		this.webDeviceName = webDeviceName;
		this.webPublicKey = webPublicKey;
		this.status = status;
		this.expiresAt = expiresAt;
		this.usedAt = usedAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static WebLoginToken createNew(String tokenHash, UUID webDeviceId, String webDeviceName, String webPublicKey,
			Instant now, Instant expiresAt) {
		return new WebLoginToken(UUID.randomUUID(), tokenHash, webDeviceId, webDeviceName, webPublicKey,
				WebLoginTokenStatus.PENDING, expiresAt, null, now, now);
	}

	public static WebLoginToken reconstitute(UUID id, String tokenHash, UUID webDeviceId, String webDeviceName,
			String webPublicKey, WebLoginTokenStatus status, Instant expiresAt, Instant usedAt, Instant createdAt,
			Instant updatedAt) {
		return new WebLoginToken(id, tokenHash, webDeviceId, webDeviceName, webPublicKey, status, expiresAt, usedAt,
				createdAt, updatedAt);
	}

	public boolean isExpired(Instant now) {
		return !expiresAt.isAfter(now);
	}

	public boolean isUsed() {
		return status == WebLoginTokenStatus.USED || usedAt != null;
	}

	public void markUsed(Instant now) {
		this.status = WebLoginTokenStatus.USED;
		this.usedAt = now;
		this.updatedAt = now;
	}
}
