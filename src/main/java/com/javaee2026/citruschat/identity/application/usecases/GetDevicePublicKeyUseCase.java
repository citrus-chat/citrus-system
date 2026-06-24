package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.results.DevicePublicKeyResult;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;

import java.util.UUID;

public class GetDevicePublicKeyUseCase {

	private final IUserDeviceRepository deviceRepository;

	public GetDevicePublicKeyUseCase(IUserDeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	public DevicePublicKeyResult execute(UUID deviceId) {
		UserDevice device = deviceRepository.findActiveById(deviceId)
				.orElseThrow(() -> new IllegalArgumentException("Device not found"));

		return new DevicePublicKeyResult(device.getId().value(), device.getPublicKey().value());
	}
}
