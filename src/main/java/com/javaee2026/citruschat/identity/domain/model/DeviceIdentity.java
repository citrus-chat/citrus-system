package com.javaee2026.citruschat.identity.domain.model;

import com.javaee2026.citruschat.identity.domain.valueobjects.PublicIdentityKey;
import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeviceIdentity {

	@EqualsAndHashCode.Include
	private final DeviceId deviceId;

	private final PublicIdentityKey publicIdentityKey;

	private final Instant createdAt;

	private DeviceIdentity(DeviceId deviceId, PublicIdentityKey publicIdentityKey, Instant createdAt) {
		this.deviceId = requireNonNull(deviceId, ErrorMessages.DEVICE_ID_CANNOT_BE_NULL);
		this.publicIdentityKey = requireNonNull(publicIdentityKey, "Public identity key cannot be null");
		this.createdAt = requireNonNull(createdAt, "Created at cannot be null");
	}

	public static DeviceIdentity createNew(DeviceId deviceId, PublicIdentityKey publicIdentityKey, Instant now) {
		return new DeviceIdentity(deviceId, publicIdentityKey, now);
	}

	public static DeviceIdentity reconstitute(DeviceId deviceId, PublicIdentityKey publicIdentityKey,
			Instant createdAt) {
		return new DeviceIdentity(deviceId, publicIdentityKey, createdAt);
	}
}
