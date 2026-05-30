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
	private String publicIdentityKey;
	private String signedPrekey;
	private Instant lastSeen;
	private final Instant createdAt;
	private final Instant revokedAt;

	private UserDevice(DeviceId id, UserId userId, String deviceName, DeviceType deviceType, String publicIdentityKey,
			String signedPrekey, Instant lastSeen, Instant createdAt, Instant revokedAt) {
		this.id = id;
		this.userId = userId;
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.publicIdentityKey = publicIdentityKey;
		this.signedPrekey = signedPrekey;
		this.lastSeen = lastSeen;
		this.createdAt = createdAt;
		this.revokedAt = revokedAt;
	}

	public static UserDevice createNew(UserId userId, String deviceName, DeviceType deviceType,
			String publicIdentityKey, String signedPrekey, Instant now) {
		return new UserDevice(DeviceId.newId(), userId, deviceName, deviceType, publicIdentityKey, signedPrekey, now,
				now, null);
	}

	public static UserDevice reconstitute(DeviceId id, UserId userId, String deviceName, DeviceType deviceType,
			String publicIdentityKey, String signedPrekey, Instant lastSeen, Instant createdAt, Instant revokedAt) {
		return new UserDevice(id, userId, deviceName, deviceType, publicIdentityKey, signedPrekey, lastSeen, createdAt,
				revokedAt);
	}

	public void refreshLastSeen(Instant now) {
		this.lastSeen = now;
	}

	public boolean isRevoked() {
		return revokedAt != null;
	}
	public void refresh(String deviceName, String publicIdentityKey, String signedPrekey, Instant now) {
		this.deviceName = deviceName;
		this.publicIdentityKey = publicIdentityKey;
		this.signedPrekey = signedPrekey;
		this.lastSeen = now;
	}
}
