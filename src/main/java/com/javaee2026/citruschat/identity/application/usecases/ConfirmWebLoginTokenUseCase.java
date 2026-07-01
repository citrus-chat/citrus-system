package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.ConfirmWebLoginTokenCommand;
import com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
import com.javaee2026.citruschat.identity.application.exceptions.ExpiredWebLoginTokenException;
import com.javaee2026.citruschat.identity.application.exceptions.InvalidWebLoginTokenException;
import com.javaee2026.citruschat.identity.application.exceptions.UsedWebLoginTokenException;
import com.javaee2026.citruschat.identity.application.exceptions.UserNotFoundException;
import com.javaee2026.citruschat.identity.application.exceptions.WebLoginDeviceNotFoundException;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.ports.IWebLoginSessionNotifier;
import com.javaee2026.citruschat.identity.application.ports.IWebLoginTokenRepository;
import com.javaee2026.citruschat.identity.application.results.ConfirmWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.results.RegisterOrRefreshUserDeviceResult;
import com.javaee2026.citruschat.identity.application.results.WebLoginSessionResult;
import com.javaee2026.citruschat.identity.application.security.WebLoginPrincipalName;
import com.javaee2026.citruschat.identity.application.security.WebLoginTokenSecurity;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.model.WebLoginToken;
import com.javaee2026.citruschat.identity.infrastructure.security.jwt.JwtService;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;

public class ConfirmWebLoginTokenUseCase {

	private final IWebLoginTokenRepository webLoginTokenRepository;
	private final IUserDeviceRepository userDeviceRepository;
	private final IUserRepository userRepository;
	private final RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase;
	private final WebLoginTokenSecurity webLoginTokenSecurity;
	private final JwtService jwtService;
	private final IWebLoginSessionNotifier webLoginSessionNotifier;

	public ConfirmWebLoginTokenUseCase(IWebLoginTokenRepository webLoginTokenRepository,
			IUserDeviceRepository userDeviceRepository, IUserRepository userRepository,
			RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase,
			WebLoginTokenSecurity webLoginTokenSecurity, JwtService jwtService,
			IWebLoginSessionNotifier webLoginSessionNotifier) {
		this.webLoginTokenRepository = webLoginTokenRepository;
		this.userDeviceRepository = userDeviceRepository;
		this.userRepository = userRepository;
		this.registerOrRefreshUserDeviceUseCase = registerOrRefreshUserDeviceUseCase;
		this.webLoginTokenSecurity = webLoginTokenSecurity;
		this.jwtService = jwtService;
		this.webLoginSessionNotifier = webLoginSessionNotifier;
	}

	@Transactional
	public ConfirmWebLoginTokenResult execute(ConfirmWebLoginTokenCommand command) {
		Instant now = Instant.now();
		String tokenHash = webLoginTokenSecurity.hashToken(command.token());

		WebLoginToken webLoginToken = webLoginTokenRepository.findByTokenHashForUpdate(tokenHash)
				.orElseThrow(InvalidWebLoginTokenException::new);

		validateToken(webLoginToken, now);
		validateDevice(webLoginToken, command);
		User user = userRepository.findById(new UserId(command.authenticatedUserId()))
				.orElseThrow(UserNotFoundException::new);

		RegisterOrRefreshUserDeviceResult deviceResult = registerOrRefreshUserDeviceUseCase.execute(
				new RegisterOrRefreshUserDeviceCommand(webLoginToken.getWebDeviceId(), command.authenticatedUserId(),
						webLoginToken.getWebPublicKey(), webLoginToken.getWebDeviceName(), DeviceType.WEB));

		webLoginToken.markUsed(now);
		webLoginTokenRepository.save(webLoginToken);
		WebLoginSessionResult session = createWebSession(user, deviceResult);
		notifyWebClientAfterCommit(WebLoginPrincipalName.fromDeviceId(webLoginToken.getWebDeviceId()), session);

		return new ConfirmWebLoginTokenResult(command.authenticatedUserId(), webLoginToken.getWebDeviceId());
	}

	private WebLoginSessionResult createWebSession(User user, RegisterOrRefreshUserDeviceResult deviceResult) {
		String accessToken = jwtService.generateToken(user.getId().value().toString(),
				deviceResult.deviceId().toString(), user.getEmail().getValue(), user.getUsername().getValue());

		return new WebLoginSessionResult(user.getId().value(), user.getEmail().getValue(),
				user.getUsername().getValue(), deviceResult.deviceId(), accessToken, "Bearer",
				jwtService.getExpirationInSeconds());
	}

	private void notifyWebClientAfterCommit(String webLoginPrincipalName, WebLoginSessionResult session) {
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				webLoginSessionNotifier.notifyLoginConfirmed(webLoginPrincipalName, session);
			}
		});
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
