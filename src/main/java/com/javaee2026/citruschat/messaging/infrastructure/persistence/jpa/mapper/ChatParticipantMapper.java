package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatParticipantJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;

import java.util.ArrayList;
import java.util.List;

public final class ChatParticipantMapper {

	public static ChatParticipant toDomain(ChatParticipantJpaEntity entity) {

		return ChatParticipant.reconstitute(new ParticipantId(entity.getId()),
				new ChatRoomId(entity.getChatRoom().getId()), new UserId(entity.getUserId()),
				entity.getRoles().stream().map(role -> new RoleId(role.getId())).toList(),

				entity.getJoinedAt(), entity.getLeftAt(),
				entity.getLastReadMessageId() != null ? new MessageId(entity.getLastReadMessageId()) : null);
	}

	public static ChatParticipantJpaEntity toJpa(ChatParticipant participant, ChatRoomJpaEntity chatRoom) {
		ChatParticipantJpaEntity entity = new ChatParticipantJpaEntity();

		entity.setId(participant.getId().value());
		entity.setChatRoom(chatRoom);
		entity.setUserId(participant.getUserId().value());

		List<ChatRoleJpaEntity> roleEntities = new ArrayList<>();

		for (ChatRoleJpaEntity role : chatRoom.getRoles()) {

			RoleId roleId = new RoleId(role.getId());

			if (participant.getRoles().contains(roleId)) {
				roleEntities.add(role);
			}
		}

		entity.setRoles(roleEntities);

		entity.setJoinedAt(participant.getJoinedAt());
		entity.setLeftAt(participant.getLeftAt());
		entity.setLastReadMessageId(
				participant.getLastReadMessageId() != null ? participant.getLastReadMessageId().value() : null);

		return entity;
	}
}
