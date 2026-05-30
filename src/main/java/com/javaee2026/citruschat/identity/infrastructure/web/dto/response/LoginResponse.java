package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import java.util.UUID;

public record LoginResponse(String userId, String email, String username, String accessToken, String tokenType,
		long expiresIn, UUID deviceId) {
}
