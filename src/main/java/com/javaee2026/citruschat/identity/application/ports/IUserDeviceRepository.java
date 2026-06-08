package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.domain.model.UserDevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserDeviceRepository {

	Optional<UserDevice> findActiveByIdAndUserId(UUID deviceId, UUID userId);

	// Optional<UserDevice> findActiveByUserIdAndDeviceType(UUID userId, DeviceType
	// deviceType);

	Optional<UserDevice> findActiveById(UUID deviceId);

	List<UserDevice> findActiveByUserId(UUID userId);

	List<UserDevice> findAllByUserId(UUID userId);

	boolean existsActiveByIdAndUserId(UUID deviceId, UUID userId);
	UserDevice save(UserDevice userDevice);
}
