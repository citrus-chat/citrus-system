package com.javaee2026.citruschat.shared.infrastructure.web;

import com.javaee2026.citruschat.shared.application.exceptions.BusinessException;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.infrastructure.web.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
		return buildErrorResponse(ex.getErrorCode().getHttpStatus(), ex.getErrorCode(), ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		Map<String, String[]> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), new String[]{error.getDefaultMessage()}));

		ApiErrorResponse response = ApiErrorResponse.validation("Validation error", HttpStatus.BAD_REQUEST.value(),
				errors, request.getRequestURI());

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.UNEXPECTED_ERROR, "Invalid request body", request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
			HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.UNEXPECTED_ERROR, ex.getMessage(), request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_ERROR, ex.getMessage(),
				request);
	}

	private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, ErrorCode errorCode, String message,
			HttpServletRequest request) {
		ApiErrorResponse error = ApiErrorResponse.of(message, status.value(), errorCode.name(),
				request.getRequestURI());

		return ResponseEntity.status(status).body(error);
	}
}
