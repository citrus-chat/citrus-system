package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

	@PostMapping(ApiRoutes.API_AUTH_LOGOUT)
	public ResponseEntity<ApiResponse<Void>> logout() {
		return ApiResponses.ok(ApiResponseMessages.LOGOUT_SUCCESS, null);
	}
}
