package com.javaee2026.citruschat.shared.infrastructure.web;

import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ApiResponses {

	private ApiResponses() {
	}

	public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
		return ResponseEntity.ok(ApiResponse.success(message, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> status(HttpStatus status, String message, T data) {
		return ResponseEntity.status(status).body(ApiResponse.success(message, data));
	}
}
