package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.UserProfileResult;
import com.javaee2026.citruschat.identity.application.usecases.GetUserProfileUseCase;
import com.javaee2026.citruschat.identity.application.usecases.UpdateUserProfileUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.UpdateUserProfileRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserProfileResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.UserProfileWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserProfileController {

	private final GetUserProfileUseCase getUserProfileUseCase;
	private final UpdateUserProfileUseCase updateUserProfileUseCase;

	public UserProfileController(GetUserProfileUseCase getUserProfileUseCase,
			UpdateUserProfileUseCase updateUserProfileUseCase) {
		this.getUserProfileUseCase = getUserProfileUseCase;
		this.updateUserProfileUseCase = updateUserProfileUseCase;
	}

	@GetMapping(ApiRoutes.API_USER_ME_PROFILE)
	public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(@AuthenticationPrincipal Jwt jwt) {

		UUID userId = UUID.fromString(jwt.getSubject());
		UserProfileResult result = getUserProfileUseCase.execute(userId);

		return ApiResponses.ok(ApiResponseMessages.USER_PROFILE_RETRIEVED_SUCCESS,
				UserProfileWebMapper.toResponse(result));
	}

	@PutMapping(ApiRoutes.API_USER_ME_PROFILE)
	public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody UpdateUserProfileRequest request) {

		UUID userId = UUID.fromString(jwt.getSubject());
		UserProfileResult result = updateUserProfileUseCase.execute(UserProfileWebMapper.toCommand(userId, request));

		return ApiResponses.ok(ApiResponseMessages.USER_PROFILE_UPDATED_SUCCESS,
				UserProfileWebMapper.toResponse(result));
	}
}
