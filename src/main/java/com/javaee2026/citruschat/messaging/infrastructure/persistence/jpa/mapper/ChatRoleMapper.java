package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ChatRoleMapper {

	private final ChatPermissionMapper chatPermissionMapper;

	public ChatRoleMapper(ChatPermissionMapper chatPermissionMapper) {
		this.chatPermissionMapper = chatPermissionMapper;
	}

	public ChatRole toDomain(ChatRoleJpaEntity entity) {

		return ChatRole.reconstitute(new RoleId(entity.getId()), new ChatRoomId(entity.getChatRoom().getId()),
				entity.getPermissions().stream().map(chatPermissionMapper::toDomain).collect(Collectors.toSet()),
				entity.getName(), entity.getPriority(), entity.getCreatedAt());
	}

	public static ChatRoleJpaEntity toJpa(ChatRole chatRole, ChatRoomJpaEntity chatRoom,
			Map<UUID, ChatPermissionJpaEntity> permissionsById) {

		ChatRoleJpaEntity entity = new ChatRoleJpaEntity();

		entity.setId(chatRole.getId().value());
		entity.setChatRoom(chatRoom);
		entity.setName(chatRole.getName());
		entity.setPriority(chatRole.getPriority());
		entity.setCreatedAt(chatRole.getCreatedAt());

		Set<ChatPermissionJpaEntity> permissions = chatRole.getRolePermissions().stream()
				.map(p -> permissionsById.get(p.getId().value())).collect(Collectors.toSet());

		entity.setPermissions(permissions);

		return entity;
	}
}
