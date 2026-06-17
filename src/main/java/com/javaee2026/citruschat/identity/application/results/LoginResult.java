package com.javaee2026.citruschat.identity.application.results;

import java.util.UUID;

//public record LoginResult(UUID userId, String email, String username, String accessToken, String tokenType,
//		long expiresIn, UUID deviceId, int availableOneTimePreKeys) {
//}

public record LoginResult(UUID userId, String email, String username, String accessToken, String tokenType,
		long expiresIn) {
}
