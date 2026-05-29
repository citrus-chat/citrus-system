package com.javaee2026.citruschat.shared.infrastructure.web.dto.response;

import com.javaee2026.citruschat.shared.domain.constants.ErrorMessages;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(boolean success, String message, int statusCode, String errorCode,
		Map<String, String[]> errors, String path, Instant timestamp) {
	public static ApiErrorResponse of(String message, int statusCode, String errorCode, String path) {
		return new ApiErrorResponse(false, message, statusCode, errorCode, null, path, Instant.now());
	}

	public static ApiErrorResponse validation(String message, int statusCode, Map<String, String[]> errors,
			String path) {
		return new ApiErrorResponse(false, message, statusCode, ErrorMessages.VALIDATION_ERROR, errors, path,
				Instant.now());
	}
}
