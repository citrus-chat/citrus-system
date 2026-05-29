package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.usecases.ValidateUserAccountUseCase;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.ValidateUserAccountRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.ValidateUserAccountResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.mapper.ValidateUserAccountWebMapper;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;

import com.javaee2026.citruschat.shared.infrastructure.web.ApiResponses;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for validating user accounts.
 */

@RestController
public class ValidateUserAccountController {

	private final ValidateUserAccountUseCase validateUserAccountUseCase;

	/**
	 * Creates a new validate user account controller.
	 *
	 * @param validateUserAccountUseCase
	 *            use case used to validate accounts
	 */
	public ValidateUserAccountController(final ValidateUserAccountUseCase validateUserAccountUseCase) {
		this.validateUserAccountUseCase = validateUserAccountUseCase;
	}

	/**
	 * Validates a user account using a temporary password.
	 *
	 * @param request
	 *            request with username, temporary password and new password
	 * @return validation response
	 */
	@PostMapping(ApiRoutes.API_AUTH_VALIDATE_ACCOUNT)
	public ResponseEntity<ApiResponse<ValidateUserAccountResponse>> validateAccount(
			@Valid @RequestBody ValidateUserAccountRequest request) {

		validateUserAccountUseCase.execute(ValidateUserAccountWebMapper.toCommand(request));

		return ApiResponses.ok(ApiResponseMessages.VALIDATE_USER_ACCOUNT_SUCCESS,
				ValidateUserAccountWebMapper.toResponse(request));
	}
}
