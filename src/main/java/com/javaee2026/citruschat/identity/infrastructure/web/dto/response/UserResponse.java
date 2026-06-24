package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaee2026.citruschat.identity.application.results.UserResult;

public record UserResponse(String id, String username, String email, boolean active,
		@JsonProperty("avatar_url") String avatarUrl) {
	public static UserResponse from(UserResult userResult) {
		return new UserResponse(userResult.getId().toString(), userResult.getUsername(), userResult.getEmail(),
				userResult.isActive(), userResult.getAvatarUrl());
	}
}
