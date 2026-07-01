package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ConversationKeyRequestJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public final class ConversationKeyRequestJpaMapper {

	private ConversationKeyRequestJpaMapper() {
	}

	public static ConversationKeyRequestJpaEntity toJpa(ConversationKeyRequest domain) {

		ConversationKeyRequestJpaEntity entity = new ConversationKeyRequestJpaEntity();

		entity.setId(null);
		entity.setConversationId(domain.conversationId().value());
		entity.setTargetUserId(domain.targetUserId().value());
		entity.setTargetDeviceId(domain.targetDeviceId().value());
		entity.setTargetPublicKey(domain.targetPublicKey());
		entity.setCreatedAt(domain.createdAt());

		return entity;
	}

	public static ConversationKeyRequest toDomain(ConversationKeyRequestJpaEntity entity) {

		return new ConversationKeyRequest(entity.getId(), new ChatRoomId(entity.getConversationId()),
				new UserId(entity.getTargetUserId()), new DeviceId(entity.getTargetDeviceId()),
				entity.getTargetPublicKey(), entity.getCreatedAt());
	}

}
