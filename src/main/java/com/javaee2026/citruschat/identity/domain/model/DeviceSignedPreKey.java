package com.javaee2026.citruschat.identity.domain.model;

import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeyPublicKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeySignature;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeviceSignedPreKey {

	@EqualsAndHashCode.Include
	private final DeviceId deviceId;

	@EqualsAndHashCode.Include
	private final int keyId;

	private final SignedPreKeyPublicKey publicKey;

	private final SignedPreKeySignature signature;

	private final Instant createdAt;

	private final Instant expiresAt;

	private DeviceSignedPreKey(DeviceId deviceId, int keyId, SignedPreKeyPublicKey publicKey,
			SignedPreKeySignature signature, Instant createdAt, Instant expiresAt) {
		this.deviceId = requireNonNull(deviceId, ErrorMessages.DEVICE_ID_CANNOT_BE_NULL);
		this.publicKey = requireNonNull(publicKey, "Signed prekey public key cannot be null");
		this.signature = requireNonNull(signature, "Signed prekey signature cannot be null");
		this.createdAt = requireNonNull(createdAt, "Created at cannot be null");

		if (keyId < 0) {
			throw new IllegalArgumentException("Signed prekey id cannot be negative");
		}

		this.keyId = keyId;
		this.expiresAt = expiresAt;
	}

	public static DeviceSignedPreKey createNew(DeviceId deviceId, int keyId, SignedPreKeyPublicKey publicKey,
			SignedPreKeySignature signature, Instant now, Instant expiresAt) {
		return new DeviceSignedPreKey(deviceId, keyId, publicKey, signature, now, expiresAt);
	}

	public static DeviceSignedPreKey reconstitute(DeviceId deviceId, int keyId, SignedPreKeyPublicKey publicKey,
			SignedPreKeySignature signature, Instant createdAt, Instant expiresAt) {
		return new DeviceSignedPreKey(deviceId, keyId, publicKey, signature, createdAt, expiresAt);
	}

	public boolean isExpired(Instant now) {
		return expiresAt != null && now.isAfter(expiresAt);
	}
}
