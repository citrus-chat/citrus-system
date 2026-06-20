package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

public record LoginResponse(String userId, String email, String username, String deviceId, String accessToken,
		String tokenType, long expiresIn) {
}
