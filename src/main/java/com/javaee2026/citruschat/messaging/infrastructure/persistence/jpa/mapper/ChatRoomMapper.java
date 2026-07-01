package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.factory.ChatRoomFactory;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatParticipantJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ChatRoomMapper {

	private final ChatRoomFactory chatRoomFactory;
	private final ChatRoleMapper chatRoleMapper;

	public ChatRoomMapper(ChatRoomFactory chatRoomFactory, ChatRoleMapper chatRoleMapper) {
		this.chatRoomFactory = chatRoomFactory;
		this.chatRoleMapper = chatRoleMapper;
	}

	public ChatRoom toDomain(ChatRoomJpaEntity entity) {
		return chatRoomFactory.reconstitute(new ChatRoomId(entity.getId()), entity.getType(), entity.getName(),
				entity.getAvatarUrl(), new UserId(entity.getCreatedBy()),
				entity.getParticipants().stream().map(ChatParticipantMapper::toDomain).toList(),
				entity.getRoles().stream().map(chatRoleMapper::toDomain)
						.collect(Collectors.toMap(ChatRole::getName, r -> r)),
				entity.getCreatedAt(), entity.getUpdatedAt(), entity.getDeletedAt());
	}

	public static ChatRoomJpaEntity toJpa(ChatRoom chatRoom, Map<UUID, ChatPermissionJpaEntity> permissionsById) {
		ChatRoomJpaEntity entity = new ChatRoomJpaEntity();

		entity.setId(chatRoom.getId().value());
		entity.setType(chatRoom.getType());
		entity.setName(chatRoom.getName());
		entity.setAvatarUrl(chatRoom.getAvatarUrl());
		entity.setCreatedBy(chatRoom.getCreatedBy().value());

		List<ChatRoleJpaEntity> roles = new ArrayList<>();

		for (ChatRole role : chatRoom.getRoles().values()) {

			ChatRoleJpaEntity roleEntity = ChatRoleMapper.toJpa(role, entity, permissionsById);

			roleEntity.setChatRoom(entity);

			roles.add(roleEntity);
		}

		entity.setRoles(roles);

		List<ChatParticipantJpaEntity> participants = new ArrayList<>();

		for (ChatParticipant participant : chatRoom.getParticipants()) {

			ChatParticipantJpaEntity participantEntity = ChatParticipantMapper.toJpa(participant, entity);

			participants.add(participantEntity);
		}

		entity.setParticipants(participants);

		entity.setCreatedAt(chatRoom.getCreatedAt());
		entity.setUpdatedAt(chatRoom.getUpdatedAt());
		entity.setDeletedAt(chatRoom.getDeletedAt());

		return entity;
	}
}
