package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.usecases.GetCurrentUserUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.CurrentUserResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.CurrentUserWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CurrentUserController {

	private final GetCurrentUserUseCase getCurrentUserUseCase;

	public CurrentUserController(GetCurrentUserUseCase getCurrentUserUseCase) {
		this.getCurrentUserUseCase = getCurrentUserUseCase;
	}

	@GetMapping(ApiRoutes.API_AUTH_ME)
	public ResponseEntity<ApiResponse<CurrentUserResponse>> me(@AuthenticationPrincipal Jwt jwt) {
		UserResult result = getCurrentUserUseCase.execute(UUID.fromString(jwt.getSubject()));

		return ApiResponses.ok(ApiResponseMessages.CURRENT_USER_SUCCESS, CurrentUserWebMapper.toResponse(result));
	}
}
