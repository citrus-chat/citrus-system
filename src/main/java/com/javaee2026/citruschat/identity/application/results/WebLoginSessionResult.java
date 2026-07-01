package com.javaee2026.citruschat.identity.application.results;

import java.util.UUID;

public record WebLoginSessionResult(UUID userId, String email, String username, UUID webDeviceId, String accessToken,
		String tokenType, long expiresIn) {
}
