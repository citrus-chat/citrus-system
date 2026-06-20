package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserResponse;

import java.util.List;

public final class UserWebMapper {

	private UserWebMapper() {
	}

	public static UserResponse toResponse(UserResult result) {
		return new UserResponse(result.getId().toString(), result.getUsername(), result.getEmail(), result.isActive());
	}

	public static List<UserResponse> toResponseList(List<UserResult> results) {
		return results.stream().map(UserWebMapper::toResponse).toList();
	}
}
