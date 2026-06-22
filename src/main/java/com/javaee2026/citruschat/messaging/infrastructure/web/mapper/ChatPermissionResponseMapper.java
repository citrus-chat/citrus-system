package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatPermissionResponse;

public final class ChatPermissionResponseMapper {

	private ChatPermissionResponseMapper() {
	}

	public static ChatPermissionResponse toResponse(ChatPermission permission) {
		return new ChatPermissionResponse(permission.getId().value(), permission.getCode(),
				permission.getDescription());
	}
}
