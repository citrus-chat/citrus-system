package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.domain.model.DeviceOneTimePreKey;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

import java.util.List;
import java.util.Optional;

public interface IDeviceOneTimePreKeyRepository {

	void save(DeviceOneTimePreKey oneTimePreKey);

	List<DeviceOneTimePreKey> findByDeviceId(DeviceId deviceId);

	Optional<DeviceOneTimePreKey> findByDeviceIdAndKeyId(DeviceId deviceId, int keyId);

	Optional<DeviceOneTimePreKey> findFirstAvailable(DeviceId deviceId);

	void deleteByDeviceId(DeviceId deviceId);

}
