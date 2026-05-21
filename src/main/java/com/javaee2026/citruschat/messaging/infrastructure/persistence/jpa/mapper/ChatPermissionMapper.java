package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.factory.ChatPermissionFactory;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;

public final class ChatPermissionMapper {

	private final ChatPermissionFactory chatPermissionFactory;

	public ChatPermissionMapper(ChatPermissionFactory chatPermissionFactory) {
		this.chatPermissionFactory = chatPermissionFactory;
	}

	public ChatPermission toDomain(ChatPermissionJpaEntity entity) {
		return chatPermissionFactory.reconstitute(new PermissionId(entity.getId()), entity.getCode(),
				entity.getDescription());
	}

	public ChatPermissionJpaEntity toJpa(ChatPermission chatPermission) {

		ChatPermissionJpaEntity entity = new ChatPermissionJpaEntity();

		entity.setId(chatPermission.getId().value());
		entity.setCode(chatPermission.getCode());
		entity.setDescription(chatPermission.getDescription());

		return entity;
	}
}
