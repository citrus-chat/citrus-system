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

	// private final GetMeUserUseCase getMeUserUseCase;
	// private final GetUserByIdUseCase getUserByIdUseCase;
	private final SearchUsersUseCase searchUsersUseCase;
	// private final CreateUserUseCase createUserUseCase;
	// private final UpdateUserUseCase updateUserUseCase;
	// private final DeleteUserUseCase deleteUserUseCase;

	public UserController(
			// GetMeUserUseCase getMeUserUseCase,
			// GetUserByIdUseCase getUserByIdUseCase,
			SearchUsersUseCase searchUsersUseCase
	// CreateUserUseCase createUserUseCase,
	// UpdateUserUseCase updateUserUseCase,
	// DeleteUserUseCase deleteUserUseCase
	) {
		// this.getMeUserUseCase = getMeUserUseCase;
		// this.getUserByIdUseCase = getUserByIdUseCase;
		this.searchUsersUseCase = searchUsersUseCase;
		// this.createUserUseCase = createUserUseCase;
		// this.updateUserUseCase = updateUserUseCase;
		// this.deleteUserUseCase = deleteUserUseCase;
	}

	// @GetMapping(ApiRoutes.API_USER_ME)
	// public ResponseEntity<ApiResponse<UserResponse>> me() {
	// UserResult result = getMeUserUseCase.execute();
	//
	// return ApiResponses.ok(
	// ApiResponseMessages.USER_RETRIEVED_SUCCESS,
	// UserWebMapper.toResponse(result)
	// );
	// }
	//
	// @GetMapping(ApiRoutes.API_USER_BY_ID)
	// public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID
	// id) {
	// UserResult result = getUserByIdUseCase.execute(id);
	//
	// return ApiResponses.ok(
	// ApiResponseMessages.USER_RETRIEVED_SUCCESS,
	// UserWebMapper.toResponse(result)
	// );
	// }

	@GetMapping(ApiRoutes.API_USERS_SEARCH)
	public ResponseEntity<ApiResponse<List<UserResponse>>> list(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		List<UserResult> results = searchUsersUseCase.execute(search, page, size);

		return ApiResponses.ok(ApiResponseMessages.USER_LIST_RETRIEVED_SUCCESS, UserWebMapper.toResponseList(results));
	}

	// @PostMapping(ApiRoutes.API_USER_CREATE)
	// public ResponseEntity<ApiResponse<UserResponse>> create(
	// @Valid @RequestBody CreateUserRequest request
	// ) {
	// UserResult result =
	// createUserUseCase.execute(UserWebMapper.toCommand(request));
	//
	// return ApiResponses.ok(
	// ApiResponseMessages.USER_CREATED_SUCCESS,
	// UserWebMapper.toResponse(result)
	// );
	// }
	//
	// @PutMapping(ApiRoutes.API_USER_UPDATE)
	// public ResponseEntity<ApiResponse<UserResponse>> update(
	// @PathVariable UUID id,
	// @Valid @RequestBody UpdateUserRequest request
	// ) {
	// UserResult result = updateUserUseCase.execute(id,
	// UserWebMapper.toCommand(request));
	//
	// return ApiResponses.ok(
	// ApiResponseMessages.USER_UPDATED_SUCCESS,
	// UserWebMapper.toResponse(result)
	// );
	// }
	//
	// @DeleteMapping(ApiRoutes.API_USER_DELETE)
	// public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
	// deleteUserUseCase.execute(id);
	//
	// return ApiResponses.ok(
	// ApiResponseMessages.USER_DELETED_SUCCESS,
	// null
	// );
	// }
}
