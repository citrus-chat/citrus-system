package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.constants.WebLoginSocketContract;
import com.javaee2026.citruschat.identity.application.commands.CreateWebLoginTokenCommand;
import com.javaee2026.citruschat.identity.application.commands.ConfirmWebLoginTokenCommand;
import com.javaee2026.citruschat.identity.application.results.CreateWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.results.ConfirmWebLoginTokenResult;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.CreateWebLoginTokenRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.CreateWebLoginTokenResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.ConfirmWebLoginTokenResponse;

import java.util.UUID;

public final class WebLoginTokenWebMapper {

	private WebLoginTokenWebMapper() {
	}

	public static CreateWebLoginTokenCommand toCommand(CreateWebLoginTokenRequest request) {
		return new CreateWebLoginTokenCommand(request.webDeviceId(), request.deviceName(), request.publicKey());
	}

	public static ConfirmWebLoginTokenCommand toCommand(String token, UUID authenticatedUserId) {
		return new ConfirmWebLoginTokenCommand(token, authenticatedUserId);
	}

	public static CreateWebLoginTokenResponse toResponse(CreateWebLoginTokenResult result) {
		return new CreateWebLoginTokenResponse(result.token(), result.webDeviceId().toString(),
				result.expiresAt().toString(), result.qrPayload(), WebLoginSocketContract.TOKEN_HEADER,
				WebLoginSocketContract.CLIENT_USER_QUEUE);
	}

	public static ConfirmWebLoginTokenResponse toResponse(ConfirmWebLoginTokenResult result) {
		return new ConfirmWebLoginTokenResponse(result.userId().toString(), result.webDeviceId().toString());
	}
}
