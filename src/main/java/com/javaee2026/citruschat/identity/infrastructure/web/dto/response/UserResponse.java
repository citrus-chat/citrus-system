package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(String id, String username, String email, boolean active,
		@JsonProperty("avatar_url") String avatarUrl) {
}
