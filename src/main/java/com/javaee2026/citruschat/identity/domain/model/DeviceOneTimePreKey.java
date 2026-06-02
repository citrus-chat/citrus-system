package com.javaee2026.citruschat.identity.domain.model;

import com.javaee2026.citruschat.identity.domain.valueobjects.OneTimePreKeyPublicKey;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeviceOneTimePreKey {

	@EqualsAndHashCode.Include
	private final DeviceId deviceId;

	@EqualsAndHashCode.Include
	private final int keyId;

	private final OneTimePreKeyPublicKey publicKey;

	private final Instant createdAt;

	private Instant consumedAt;

	private DeviceOneTimePreKey(DeviceId deviceId, int keyId, OneTimePreKeyPublicKey publicKey, Instant createdAt,
			Instant consumedAt) {
		this.deviceId = requireNonNull(deviceId, "Device id cannot be null");
		this.publicKey = requireNonNull(publicKey, "One-time prekey public key cannot be null");
		this.createdAt = requireNonNull(createdAt, "Created at cannot be null");

		if (keyId < 0) {
			throw new IllegalArgumentException("One-time prekey id cannot be negative");
		}

		this.keyId = keyId;
		this.consumedAt = consumedAt;
	}

	public static DeviceOneTimePreKey createNew(DeviceId deviceId, int keyId, OneTimePreKeyPublicKey publicKey,
			Instant now) {
		return new DeviceOneTimePreKey(deviceId, keyId, publicKey, now, null);
	}

	public static DeviceOneTimePreKey reconstitute(DeviceId deviceId, int keyId, OneTimePreKeyPublicKey publicKey,
			Instant createdAt, Instant consumedAt) {
		return new DeviceOneTimePreKey(deviceId, keyId, publicKey, createdAt, consumedAt);
	}

	public boolean isConsumed() {
		return consumedAt != null;
	}

	public void markAsConsumed(Instant now) {
		if (isConsumed()) {
			throw new IllegalStateException("One-time prekey has already been consumed");
		}

		this.consumedAt = requireNonNull(now);
	}
}
