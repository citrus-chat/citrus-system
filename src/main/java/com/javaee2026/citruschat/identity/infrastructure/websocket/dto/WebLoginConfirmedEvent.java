package com.javaee2026.citruschat.identity.infrastructure.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WebLoginConfirmedEvent(String type,

		@JsonProperty("user_id") String userId,

		String email,

		String username,

		@JsonProperty("device_id") String deviceId,

		@JsonProperty("access_token") String accessToken,

		@JsonProperty("token_type") String tokenType,

		@JsonProperty("expires_in") long expiresIn) {
}
