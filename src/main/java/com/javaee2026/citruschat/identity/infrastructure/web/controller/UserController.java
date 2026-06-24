package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.UserResult;
//import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.CreateUserRequest;
//import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.UpdateUserRequest;
//import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserResponse;
//import com.javaee2026.citruschat.identity.infrastructure.web.mapper.UserWebMapper;
import com.javaee2026.citruschat.identity.application.usecases.SearchUsersUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.UserWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

	private final SearchUsersUseCase searchUsersUseCase;

	public UserController(SearchUsersUseCase searchUsersUseCase) {
		this.searchUsersUseCase = searchUsersUseCase;
	}

	@GetMapping(ApiRoutes.API_USERS_SEARCH)
	public ResponseEntity<ApiResponse<List<UserResponse>>> list(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		List<UserResult> results = searchUsersUseCase.execute(search, page, size);

		return ApiResponses.ok(ApiResponseMessages.USER_LIST_RETRIEVED_SUCCESS, UserWebMapper.toResponseList(results));
	}
}
