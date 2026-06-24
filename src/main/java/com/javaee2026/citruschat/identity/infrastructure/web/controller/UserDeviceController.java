package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

	public UserDeviceController(GetDevicePublicKeyUseCase getDevicePublicKeyUseCase) {
		this.getDevicePublicKeyUseCase = getDevicePublicKeyUseCase;
	}

	@GetMapping(ApiRoutes.API_DEVICE_KEYS)
	public ResponseEntity<ApiResponse<DevicePublicKeyResponse>> getKeys(@PathVariable UUID deviceId) {

		DevicePublicKeyResult result = getDevicePublicKeyUseCase.execute(deviceId);

		return ApiResponses.ok(ApiResponseMessages.DEVICE_KEYS_RETRIEVED_SUCCESS,
				UserDeviceWebMapper.toDevicePublicKeyResponse(result));
	}
}
