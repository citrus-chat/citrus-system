package com.javaee2026.citruschat.shared.infrastructure.web.dto.response;

import java.time.Instant;

public record ApiResponse<T>(boolean success, String message, T data, Instant timestamp) {
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data, Instant.now());
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, null, data, Instant.now());
	}

	public static ApiResponse<Void> success(String message) {
		return new ApiResponse<>(true, message, null, Instant.now());
	}
}
