package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoleResponse;

import java.util.UUID;

public final class ChatRoleResponseMapper {

	private ChatRoleResponseMapper() {
	}

	public static ChatRoleResponse toResponse(ChatRole role) {
		UUID chatRoomId = role.getChatRoomId() != null ? role.getChatRoomId().value() : null;
		// ponytail: ChatRole has no persisted updatedAt yet; add it to the
		// aggregate/entity when edits need audit history.
		return new ChatRoleResponse(role.getId().value(), chatRoomId, role.getName(), role.getPriority(),
				role.getRolePermissions().stream().map(ChatPermissionResponseMapper::toResponse).toList(),
				role.getCreatedAt(), role.getCreatedAt());
	}
}
