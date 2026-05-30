package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.commands.LoginCommand;
import com.javaee2026.citruschat.identity.application.results.LoginResult;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.LoginRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.LoginResponse;

public final class LoginWebMapper {

	private LoginWebMapper() {
	}

	public static LoginCommand toCommand(LoginRequest request) {
		return new LoginCommand(request.email(), request.password(), request.deviceId(), request.deviceName(),
				request.deviceType(), request.publicIdentityKey(), request.signedPrekey());
	}

	public static LoginResponse toResponse(LoginResult result) {
		return new LoginResponse(result.userId().toString(), result.email(), result.username(), result.accessToken(),
				result.tokenType(), result.expiresIn(), result.deviceId());
	}
}
