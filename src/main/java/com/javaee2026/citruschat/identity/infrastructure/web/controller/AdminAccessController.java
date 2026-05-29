package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.infrastructure.security.AdminSecurityService;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.AdminAccessResponse;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminAccessController {

	private final AdminSecurityService adminSecurityService;

	public AdminAccessController(AdminSecurityService adminSecurityService) {
		this.adminSecurityService = adminSecurityService;
	}

	@GetMapping(ApiRoutes.API_ADMIN_ACCESS)
	public ResponseEntity<ApiResponse<AdminAccessResponse>> checkAdminAccess(Authentication authentication) {
		boolean isAdmin = adminSecurityService.isAdmin(authentication);

		AdminAccessResponse response = new AdminAccessResponse(isAdmin);

		return ApiResponses.ok(ApiResponseMessages.ADMIN_ACCESS_CHECKED_SUCCESSFULLY, response);
	}
}
