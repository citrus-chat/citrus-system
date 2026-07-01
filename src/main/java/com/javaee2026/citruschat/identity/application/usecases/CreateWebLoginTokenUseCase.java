package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.CreateWebLoginTokenCommand;
import com.javaee2026.citruschat.identity.application.ports.IWebLoginTokenRepository;
import com.javaee2026.citruschat.identity.application.results.CreateWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.security.WebLoginTokenSecurity;
import com.javaee2026.citruschat.identity.domain.model.WebLoginToken;

import java.time.Duration;
import java.time.Instant;

public class CreateWebLoginTokenUseCase {

	private static final Duration TOKEN_TTL = Duration.ofMinutes(10);

	private final IWebLoginTokenRepository webLoginTokenRepository;
	private final WebLoginTokenSecurity webLoginTokenSecurity;

	public CreateWebLoginTokenUseCase(IWebLoginTokenRepository webLoginTokenRepository,
			WebLoginTokenSecurity webLoginTokenSecurity) {
		this.webLoginTokenRepository = webLoginTokenRepository;
		this.webLoginTokenSecurity = webLoginTokenSecurity;
	}

	public CreateWebLoginTokenResult execute(CreateWebLoginTokenCommand command) {
		Instant now = Instant.now();
		Instant expiresAt = now.plus(TOKEN_TTL);
		String token = generateUniqueToken();
		String tokenHash = webLoginTokenSecurity.hashToken(token);

		WebLoginToken webLoginToken = WebLoginToken.createNew(tokenHash, command.webDeviceId(),
				normalizeDeviceName(command.webDeviceName()), command.webPublicKey(), now, expiresAt);

		WebLoginToken saved = webLoginTokenRepository.save(webLoginToken);

		return new CreateWebLoginTokenResult(token, saved.getWebDeviceId(), saved.getExpiresAt(), token);
	}

	private String generateUniqueToken() {
		String token = webLoginTokenSecurity.generateToken();
		String tokenHash = webLoginTokenSecurity.hashToken(token);

		while (webLoginTokenRepository.existsByTokenHash(tokenHash)) {
			token = webLoginTokenSecurity.generateToken();
			tokenHash = webLoginTokenSecurity.hashToken(token);
		}

		return token;
	}

	private String normalizeDeviceName(String deviceName) {
		if (deviceName == null || deviceName.isBlank()) {
			return "Unknown web device";
		}

		return deviceName.trim();
	}
}
