package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.CreateWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.results.ConfirmWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.usecases.CreateWebLoginTokenUseCase;
import com.javaee2026.citruschat.identity.application.usecases.ConfirmWebLoginTokenUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.CreateWebLoginTokenRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.ConfirmWebLoginTokenRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.CreateWebLoginTokenResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.ConfirmWebLoginTokenResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.WebLoginTokenWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class WebLoginTokenController {

	private final CreateWebLoginTokenUseCase createWebLoginTokenUseCase;
	private final ConfirmWebLoginTokenUseCase confirmWebLoginTokenUseCase;

	public WebLoginTokenController(CreateWebLoginTokenUseCase createWebLoginTokenUseCase,
			ConfirmWebLoginTokenUseCase confirmWebLoginTokenUseCase) {
		this.createWebLoginTokenUseCase = createWebLoginTokenUseCase;
		this.confirmWebLoginTokenUseCase = confirmWebLoginTokenUseCase;
	}

	@PostMapping(ApiRoutes.API_WEB_LOGIN_TOKENS)
	public ResponseEntity<ApiResponse<CreateWebLoginTokenResponse>> create(
			@Valid @RequestBody CreateWebLoginTokenRequest request) {
		CreateWebLoginTokenResult result = createWebLoginTokenUseCase
				.execute(WebLoginTokenWebMapper.toCommand(request));

		return ApiResponses.created(ApiResponseMessages.WEB_LOGIN_TOKEN_CREATED,
				WebLoginTokenWebMapper.toResponse(result));
	}

	@PostMapping(ApiRoutes.API_WEB_LOGIN_TOKENS_CONFIRM)
	public ResponseEntity<ApiResponse<ConfirmWebLoginTokenResponse>> confirm(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody ConfirmWebLoginTokenRequest request) {
		ConfirmWebLoginTokenResult result = confirmWebLoginTokenUseCase
				.execute(WebLoginTokenWebMapper.toCommand(request.token(), UUID.fromString(jwt.getSubject())));

		return ApiResponses.ok(ApiResponseMessages.WEB_LOGIN_TOKEN_CONFIRMED,
				WebLoginTokenWebMapper.toResponse(result));
	}
}
