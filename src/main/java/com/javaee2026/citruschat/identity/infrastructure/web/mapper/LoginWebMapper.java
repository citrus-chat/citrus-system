package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.commands.LoginCommand;
import com.javaee2026.citruschat.identity.application.dto.DeviceInfo;
import com.javaee2026.citruschat.identity.application.results.LoginResult;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.LoginRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.LoginResponse;

public final class LoginWebMapper {

	private LoginWebMapper() {
	}

	public static LoginCommand toCommand(LoginRequest request) {
		return new LoginCommand(request.email(), request.password(),
				new DeviceInfo(request.deviceRequest().deviceId(), request.deviceRequest().deviceName(),
						request.deviceRequest().deviceType(), new PublicKey(request.deviceRequest().publicKey())));
	}

	public static LoginResponse toResponse(LoginResult result) {
		return new LoginResponse(result.userId().toString(), result.email(), result.username(),
				result.deviceId().toString(), result.accessToken(), result.tokenType(), result.expiresIn());
	}
}
