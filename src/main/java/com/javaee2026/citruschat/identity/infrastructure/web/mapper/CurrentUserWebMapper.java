package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.CurrentUserResponse;

public final class CurrentUserWebMapper {

	private CurrentUserWebMapper() {
	}

	public static CurrentUserResponse toResponse(UserResult result) {
		return new CurrentUserResponse(result.getId().toString(), result.getEmail(), result.getUsername(),
				result.getAvatarUrl());
	}
}
