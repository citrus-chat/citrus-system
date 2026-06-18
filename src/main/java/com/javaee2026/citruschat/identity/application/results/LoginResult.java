package com.javaee2026.citruschat.identity.application.results;

import java.util.UUID;

public record LoginResult(UUID userId, String email, String username, UUID deviceId, String accessToken,
		String tokenType, long expiresIn) {
}
