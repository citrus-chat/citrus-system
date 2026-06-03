package com.javaee2026.citruschat.identity.domain.model;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import lombok.Getter;

import java.time.Instant;

@Getter
public class UserDevice {

	private final DeviceId id;
	private final UserId userId;

	private String deviceName;

	private final DeviceType deviceType;

	private Instant lastSeen;

	private final Instant createdAt;

	private Instant revokedAt;

	private UserDevice(DeviceId id, UserId userId, String deviceName, DeviceType deviceType, Instant lastSeen,
			Instant createdAt, Instant revokedAt) {
		this.id = id;
		this.userId = userId;
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.lastSeen = lastSeen;
		this.createdAt = createdAt;
		this.revokedAt = revokedAt;
	}

	public static UserDevice createNew(UserId userId, String deviceName, DeviceType deviceType, Instant now) {
		return new UserDevice(DeviceId.newId(), userId, deviceName, deviceType, now, now, null);
	}

	public static UserDevice reconstitute(DeviceId id, UserId userId, String deviceName, DeviceType deviceType,
			Instant lastSeen, Instant createdAt, Instant revokedAt) {
		return new UserDevice(id, userId, deviceName, deviceType, lastSeen, createdAt, revokedAt);
	}

	public void refreshLastSeen(Instant now) {
		this.lastSeen = now;
	}

	public void rename(String deviceName) {
		if (this.deviceName.equals(deviceName)) { // If the device's name does not change, don't rename it
			return;
		}

		this.deviceName = deviceName;
	}

	public void revoke(Instant now) {
		this.revokedAt = now;
	}

	public boolean isRevoked() {
		return revokedAt != null;
	}
}
