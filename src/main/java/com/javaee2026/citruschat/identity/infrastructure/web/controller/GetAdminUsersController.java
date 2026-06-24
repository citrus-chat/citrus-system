package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.dto.UserPageQuery;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.usecases.GetAdminUsersUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserResponse;
import com.javaee2026.citruschat.shared.application.results.PagedResult;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.PagedResponse;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.PaginationMetaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetAdminUsersController {

	private final GetAdminUsersUseCase getAdminUsersUseCase;

	public GetAdminUsersController(GetAdminUsersUseCase getAdminUsersUseCase) {
		this.getAdminUsersUseCase = getAdminUsersUseCase;
	}

	@GetMapping(ApiRoutes.API_ADMIN_USERS)
	public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> getAdminUsers(
			@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "20", required = false) int size,
			@RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
		UserPageQuery query = UserPageQuery.of(normalizePage(page), normalizeSize(size), sortBy, direction);
		PagedResult<UserResult> result = getAdminUsersUseCase.execute(query);

		PagedResponse<UserResponse> response = new PagedResponse<>(
				result.items().stream().map(UserResponse::from).toList(), PaginationMetaResponse.from(result));

		return ApiResponses.ok(ApiResponseMessages.ADMIN_USERS_RETRIEVED_SUCCESS, response);
	}

	private int normalizePage(int page) {
		return Math.max(page, 0);
	}

	private int normalizeSize(int size) {
		if (size < 1) {
			return 20;
		}

		return Math.min(size, 100);
	}
}
