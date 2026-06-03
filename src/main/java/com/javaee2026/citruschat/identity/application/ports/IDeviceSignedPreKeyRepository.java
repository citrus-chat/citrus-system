package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.domain.model.DeviceSignedPreKey;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IDeviceSignedPreKeyRepository {

	void save(DeviceSignedPreKey signedPreKey);

	List<DeviceSignedPreKey> findByDeviceId(DeviceId deviceId);

	Optional<DeviceSignedPreKey> findActiveByDeviceId(DeviceId deviceId, Instant now);

	Optional<DeviceSignedPreKey> findByDeviceIdAndKeyId(DeviceId deviceId, int keyId);

	void deleteByDeviceId(DeviceId deviceId);
}
