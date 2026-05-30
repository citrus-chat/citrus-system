package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.results.RegisterOrRefreshUserDeviceResult;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;

public class RegisterOrRefreshUserDeviceUseCase {

	private final IUserDeviceRepository userDeviceRepository;

	public RegisterOrRefreshUserDeviceUseCase(IUserDeviceRepository userDeviceRepository) {
		this.userDeviceRepository = userDeviceRepository;
	}

	public RegisterOrRefreshUserDeviceResult execute(RegisterOrRefreshUserDeviceCommand command) {
		Instant now = Instant.now();
		DeviceType deviceType = command.deviceType() != null ? command.deviceType() : DeviceType.web;

		if (command.deviceId() != null) {
			var existingById = userDeviceRepository.findActiveByIdAndUserId(command.deviceId(), command.userId());

			if (existingById.isPresent()) {
				UserDevice device = existingById.get();
				refreshDevice(device, command, now);

				UserDevice saved = userDeviceRepository.save(device);
				return new RegisterOrRefreshUserDeviceResult(saved.getId().value());
			}
		}

		var existingByType = userDeviceRepository.findActiveByUserIdAndDeviceType(command.userId(), deviceType);

		if (existingByType.isPresent()) {
			UserDevice device = existingByType.get();
			refreshDevice(device, command, now);

			UserDevice saved = userDeviceRepository.save(device);
			return new RegisterOrRefreshUserDeviceResult(saved.getId().value());
		}

		UserDevice newDevice = UserDevice.createNew(new UserId(command.userId()),
				normalizeDeviceName(command.deviceName()), deviceType, command.publicIdentityKey(),
				command.signedPrekey(), now);

		UserDevice saved = userDeviceRepository.save(newDevice);
		return new RegisterOrRefreshUserDeviceResult(saved.getId().value());
	}

	private void refreshDevice(UserDevice device, RegisterOrRefreshUserDeviceCommand command, Instant now) {
		device.refresh(normalizeDeviceName(command.deviceName()), command.publicIdentityKey(), command.signedPrekey(),
				now);
	}

	private String normalizeDeviceName(String deviceName) {
		if (deviceName == null || deviceName.isBlank()) {
			return "Unknown device";
		}

		return deviceName.trim();
	}
}
