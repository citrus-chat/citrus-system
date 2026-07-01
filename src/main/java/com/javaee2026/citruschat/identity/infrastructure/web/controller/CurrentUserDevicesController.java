package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.usecases.GetCurrentUserDevicesUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserDeviceResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.UserDeviceWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CurrentUserDevicesController {

	private final GetCurrentUserDevicesUseCase getCurrentUserDevicesUseCase;

	public CurrentUserDevicesController(GetCurrentUserDevicesUseCase getCurrentUserDevicesUseCase) {
		this.getCurrentUserDevicesUseCase = getCurrentUserDevicesUseCase;
	}

	@GetMapping(ApiRoutes.API_AUTH_DEVICES)
	public ResponseEntity<ApiResponse<List<UserDeviceResponse>>> getMyDevices(@AuthenticationPrincipal Jwt jwt) {
		UUID userId = UUID.fromString(jwt.getSubject());

		List<UserDeviceResponse> devices = getCurrentUserDevicesUseCase.execute(userId).stream()
				.map(UserDeviceWebMapper::toResponse).toList();

		return ApiResponses.ok(ApiResponseMessages.USER_DEVICES_RETRIEVED_SUCCESS, devices);
	}
}