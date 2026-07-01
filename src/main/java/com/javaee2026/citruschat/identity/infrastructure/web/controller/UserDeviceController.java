package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import java.util.UUID;

import com.javaee2026.citruschat.messaging.application.usecases.RequestConversationKeyUseCase;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.RequestConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.mapper.RequestConversationKeyWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.javaee2026.citruschat.identity.application.results.DevicePublicKeyResult;
import com.javaee2026.citruschat.identity.application.usecases.GetDevicePublicKeyUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.DevicePublicKeyResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.UserDeviceWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

@RestController
public class UserDeviceController {

	private final GetDevicePublicKeyUseCase getDevicePublicKeyUseCase;
	private final RequestConversationKeyUseCase requestConversationKeyUseCase;

	public UserDeviceController(GetDevicePublicKeyUseCase getDevicePublicKeyUseCase,
			RequestConversationKeyUseCase requestConversationKeyUseCase) {
		this.getDevicePublicKeyUseCase = getDevicePublicKeyUseCase;
		this.requestConversationKeyUseCase = requestConversationKeyUseCase;
	}

	@GetMapping(ApiRoutes.API_DEVICE_KEYS)
	public ResponseEntity<ApiResponse<DevicePublicKeyResponse>> getKeys(@PathVariable UUID deviceId) {

		DevicePublicKeyResult result = getDevicePublicKeyUseCase.execute(deviceId);

		return ApiResponses.ok(ApiResponseMessages.DEVICE_KEYS_RETRIEVED_SUCCESS,
				UserDeviceWebMapper.toDevicePublicKeyResponse(result));
	}

	@PostMapping(ApiRoutes.API_DEVICE_REQUEST_KEYS)
	public ResponseEntity<ApiResponse<Void>> requestConversationKey(@AuthenticationPrincipal Jwt jwt,
			@Valid @RequestBody RequestConversationKeyRequest request) {

		UUID userId = UUID.fromString(jwt.getSubject());

		requestConversationKeyUseCase.execute(RequestConversationKeyWebMapper.toCommand(request), userId);

		return ApiResponses.created(ApiResponseMessages.CONVERSATION_KEY_REQUEST_CREATED, null);
	}
}
