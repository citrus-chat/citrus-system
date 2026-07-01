package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.commands.LogoutCommand;
import com.javaee2026.citruschat.identity.application.usecases.LogoutUseCase;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class LogoutController {

	private final LogoutUseCase logoutUseCase;

	public LogoutController(LogoutUseCase logoutUseCase) {
		this.logoutUseCase = logoutUseCase;
	}

	@PostMapping(ApiRoutes.API_AUTH_LOGOUT)
	public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal Jwt jwt) {
		UUID userId = UUID.fromString(jwt.getSubject());
		UUID deviceId = UUID.fromString(jwt.getClaim("deviceId"));

		logoutUseCase.execute(new LogoutCommand(deviceId, userId));

		return ApiResponses.ok(ApiResponseMessages.LOGOUT_SUCCESS, null);
	}
}
