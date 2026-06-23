package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
//import com.javaee2026.citruschat.messaging.application.results.ChatRoomSummaryResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoomMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JpaChatRoomRepositoryAdapter implements IChatRoomRepository {

	private final SpringDataChatRoomRepository chatRoomRepository;
	private final ChatRoomMapper chatRoomMapper;
	private final SpingDataChatPermissionRepository chatPermissionRepository;

	public JpaChatRoomRepositoryAdapter(SpringDataChatRoomRepository chatRoomRepository, ChatRoomMapper chatRoomMapper,
			SpingDataChatPermissionRepository chatPermissionRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoomMapper = chatRoomMapper;
		this.chatPermissionRepository = chatPermissionRepository;
	}

	@Override
	@Transactional
	public void save(ChatRoom chatRoom) {

		Set<UUID> permissionIds = chatRoom.getRoles().values().stream().flatMap(r -> r.getRolePermissions().stream())
				.map(p -> p.getId().value()).collect(Collectors.toSet());

		Map<UUID, ChatPermissionJpaEntity> permissionsById = chatPermissionRepository.findAllById(permissionIds)
				.stream().collect(Collectors.toMap(ChatPermissionJpaEntity::getId, Function.identity()));

		chatRoomRepository.save(ChatRoomMapper.toJpa(chatRoom, permissionsById));
	}

	@Override
	public List<ChatRoom> findAllChatRooms(UserId user) {
		return chatRoomRepository.findAllChatRooms(user.value()).stream().map(chatRoomMapper::toDomain).toList();
	}

	@Override
	public List<ChatRoom> findChatRoomsCreatedBy(UserId user) {
		return chatRoomRepository.findByCreatedBy(user.value()).stream().map(chatRoomMapper::toDomain).toList();
	}

	@Override
	public Boolean existsDirectChatBetweenParticipants(UserId participant1, UserId participant2) {
		return chatRoomRepository.existsDirectChatBetweenParticipants(participant1.value(), participant2.value(),
				ChatRoomType.DIRECT);
	}

	// @Override
	// public List<ChatRoomSummaryResult> findActiveChatRoomsByUserId(UUID userId) {
	// return chatRoomRepository.findActiveChatRoomsByUserId(userId);
	// }

	@Override
	@Transactional
	public List<ChatRoom> findUpdatedChatRooms(UserId userId, Instant since) {
		return chatRoomRepository.findUpdatedChatRooms(userId.value(), since).stream().map(chatRoomMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<ChatRoom> findById(ChatRoomId id) {
		return chatRoomRepository.findById(id.value()).map(chatRoomMapper::toDomain);
	}
}
