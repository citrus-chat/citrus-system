package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.RegisterUserResult;
import com.javaee2026.citruschat.identity.application.usecases.RegisterUserUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.RegisterUserRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.RegisterUserResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.RegisterUserWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterUserController {

	private final RegisterUserUseCase registerUserUseCase;

	public RegisterUserController(RegisterUserUseCase registerUserUseCase) {
		this.registerUserUseCase = registerUserUseCase;
	}

	@PostMapping(ApiRoutes.API_ADMIN_USERS)
	public ResponseEntity<ApiResponse<RegisterUserResponse>> register(@Valid @RequestBody RegisterUserRequest request) {

		RegisterUserResult result = registerUserUseCase.execute(RegisterUserWebMapper.toCommand(request));

		return ApiResponses.created(ApiResponseMessages.REGISTRATION_SUCCESS, RegisterUserWebMapper.toResponse(result));
	}
}
