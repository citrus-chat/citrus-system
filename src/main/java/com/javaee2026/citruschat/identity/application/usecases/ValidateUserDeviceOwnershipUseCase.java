package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;

import java.util.UUID;

public class ValidateUserDeviceOwnershipUseCase {

	private final IUserDeviceRepository userDeviceRepository;

	public ValidateUserDeviceOwnershipUseCase(IUserDeviceRepository userDeviceRepository) {
		this.userDeviceRepository = userDeviceRepository;
	}

	public boolean execute(UUID userId, UUID deviceId) {
		if (userId == null || deviceId == null) {
			return false;
		}

		return userDeviceRepository.existsActiveByIdAndUserId(deviceId, userId);
	}
}
