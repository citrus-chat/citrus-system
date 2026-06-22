package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoleResponse;

public final class ChatRoleResponseMapper {

	private ChatRoleResponseMapper() {
	}

	public static ChatRoleResponse toResponse(ChatRole role) {
		return new ChatRoleResponse(role.getId().value(),
				role.getRolePermissions().stream().map(ChatPermissionResponseMapper::toResponse).toList(),
				role.getName(), role.getPriority(), role.getCreatedAt());
	}
}
