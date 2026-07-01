package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.ConfirmWebLoginTokenCommand;
import com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
import com.javaee2026.citruschat.identity.application.exceptions.ExpiredWebLoginTokenException;
import com.javaee2026.citruschat.identity.application.exceptions.InvalidWebLoginTokenException;
import com.javaee2026.citruschat.identity.application.exceptions.UsedWebLoginTokenException;
import com.javaee2026.citruschat.identity.application.exceptions.WebLoginDeviceNotFoundException;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IWebLoginTokenRepository;
import com.javaee2026.citruschat.identity.application.results.ConfirmWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.security.WebLoginTokenSecurity;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.model.WebLoginToken;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

public class ConfirmWebLoginTokenUseCase {

	private final IWebLoginTokenRepository webLoginTokenRepository;
	private final IUserDeviceRepository userDeviceRepository;
	private final RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase;
	private final WebLoginTokenSecurity webLoginTokenSecurity;

	public ConfirmWebLoginTokenUseCase(IWebLoginTokenRepository webLoginTokenRepository,
			IUserDeviceRepository userDeviceRepository,
			RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase,
			WebLoginTokenSecurity webLoginTokenSecurity) {
		this.webLoginTokenRepository = webLoginTokenRepository;
		this.userDeviceRepository = userDeviceRepository;
		this.registerOrRefreshUserDeviceUseCase = registerOrRefreshUserDeviceUseCase;
		this.webLoginTokenSecurity = webLoginTokenSecurity;
	}

	@Transactional
	public ConfirmWebLoginTokenResult execute(ConfirmWebLoginTokenCommand command) {
		Instant now = Instant.now();
		String tokenHash = webLoginTokenSecurity.hashToken(command.token());

		WebLoginToken webLoginToken = webLoginTokenRepository.findByTokenHashForUpdate(tokenHash)
				.orElseThrow(InvalidWebLoginTokenException::new);

		validateToken(webLoginToken, now);
		validateDevice(webLoginToken, command);

		registerOrRefreshUserDeviceUseCase.execute(
				new RegisterOrRefreshUserDeviceCommand(webLoginToken.getWebDeviceId(), command.authenticatedUserId(),
						webLoginToken.getWebPublicKey(), webLoginToken.getWebDeviceName(), DeviceType.WEB));

		webLoginToken.markUsed(now);
		webLoginTokenRepository.save(webLoginToken);

		return new ConfirmWebLoginTokenResult(command.authenticatedUserId(), webLoginToken.getWebDeviceId());
	}

	private void validateToken(WebLoginToken webLoginToken, Instant now) {
		if (webLoginToken.isUsed()) {
			throw new UsedWebLoginTokenException();
		}

		if (webLoginToken.isExpired(now)) {
			throw new ExpiredWebLoginTokenException();
		}
	}

	private void validateDevice(WebLoginToken webLoginToken, ConfirmWebLoginTokenCommand command) {
		if (webLoginToken.getWebDeviceId() == null || webLoginToken.getWebPublicKey() == null
				|| webLoginToken.getWebPublicKey().isBlank()) {
			throw new WebLoginDeviceNotFoundException();
		}

		var existingDevice = userDeviceRepository.findActiveById(webLoginToken.getWebDeviceId());

		if (existingDevice.isPresent()) {
			UserDevice device = existingDevice.get();

			if (!device.getUserId().value().equals(command.authenticatedUserId())) {
				throw new InvalidWebLoginTokenException();
			}
		}
	}
}
