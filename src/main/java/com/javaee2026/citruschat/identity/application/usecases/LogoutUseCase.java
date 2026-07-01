package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.LogoutCommand;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;

import java.time.Instant;

public class LogoutUseCase {
	private final IUserDeviceRepository userDeviceRepository;

	public LogoutUseCase(IUserDeviceRepository userDeviceRepository) {
		this.userDeviceRepository = userDeviceRepository;
	}

	public void execute(LogoutCommand command) {

		UserDevice userDevice = userDeviceRepository.findActiveByIdAndUserId(command.deviceId(), command.userId())
				.orElseThrow(() -> new IllegalArgumentException("Device not found or not active for the given user."));

		userDevice.revoke(Instant.now());

		userDeviceRepository.save(userDevice);

	}
}
