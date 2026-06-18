package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
import com.javaee2026.citruschat.identity.application.exceptions.IllegalPublicKeyException;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.results.RegisterOrRefreshUserDeviceResult;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;

public class RegisterOrRefreshUserDeviceUseCase {

	private final IUserDeviceRepository userDeviceRepository;

	public RegisterOrRefreshUserDeviceUseCase(IUserDeviceRepository userDeviceRepository) {
		this.userDeviceRepository = userDeviceRepository;
	}

	public RegisterOrRefreshUserDeviceResult execute(RegisterOrRefreshUserDeviceCommand command) {
		Instant now = Instant.now();

		DeviceType deviceType = command.deviceType() != null ? command.deviceType() : DeviceType.WEB;

		if (command.deviceId() != null) {
			var existing = userDeviceRepository.findActiveByIdAndUserId(command.deviceId(), command.userId());

			if (existing.isPresent()) {
				UserDevice device = existing.get();

				if (!device.getPublicKey().toString().equals(command.publicKey())) {
					throw new IllegalPublicKeyException();
				}

				refreshDevice(device, command, now);

				UserDevice saved = userDeviceRepository.save(device);

				return new RegisterOrRefreshUserDeviceResult(saved.getId().value());
			}
		}

		UserDevice newDevice = UserDevice.createNew(new UserId(command.userId()),
				new PublicKey(command.publicKey().toString()), normalizeDeviceName(command.deviceName()), deviceType,
				now);

		UserDevice saved = userDeviceRepository.save(newDevice);

		return new RegisterOrRefreshUserDeviceResult(saved.getId().value());
	}

	private void refreshDevice(UserDevice device, RegisterOrRefreshUserDeviceCommand command, Instant now) {
		device.rename(normalizeDeviceName(command.deviceName()));
		device.refreshLastSeen(now);
	}

	private String normalizeDeviceName(String deviceName) {
		if (deviceName == null || deviceName.isBlank()) {
			return "Unknown device";
		}

		return deviceName.trim();
	}
}
