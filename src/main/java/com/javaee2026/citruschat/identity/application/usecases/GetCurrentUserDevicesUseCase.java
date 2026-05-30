package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;

import java.util.List;
import java.util.UUID;

public class GetCurrentUserDevicesUseCase {

	private final IUserDeviceRepository userDeviceRepository;

	public GetCurrentUserDevicesUseCase(IUserDeviceRepository userDeviceRepository) {
		this.userDeviceRepository = userDeviceRepository;
	}

	public List<UserDevice> execute(UUID userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}

		return userDeviceRepository.findActiveByUserId(userId);
	}
}
