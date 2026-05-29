package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.LoginResult;
import com.javaee2026.citruschat.identity.application.usecases.LoginUserUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.LoginRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.LoginResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.LoginWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

	private final LoginUserUseCase loginUserUseCase;

	public LoginController(LoginUserUseCase loginUserUseCase) {
		this.loginUserUseCase = loginUserUseCase;
	}

	@PostMapping(ApiRoutes.API_AUTH_LOGIN)
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
		LoginResult result = loginUserUseCase.execute(LoginWebMapper.toCommand(request));

		return ApiResponses.ok(ApiResponseMessages.LOGIN_SUCCESS, LoginWebMapper.toResponse(result));
	}
}
