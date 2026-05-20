package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;

public final class ChatRoleMapper {

	public static ChatRole toDomain(ChatRoleJpaEntity entity) {

		return ChatRole.reconstitute(new RoleId(entity.getId()), new ChatRoomId(entity.getChatRoom().getId()),
				entity.getName(), entity.getCreatedAt());
	}

	public static ChatRoleJpaEntity toJpa(ChatRole chatRole, ChatRoomJpaEntity chatRoom) {

		ChatRoleJpaEntity entity = new ChatRoleJpaEntity();

		entity.setId(chatRole.getId().value());
		entity.setChatRoom(chatRoom);
		entity.setName(chatRole.getName());
		entity.setCreatedAt(chatRole.getCreatedAt());

		return entity;
	}
}
