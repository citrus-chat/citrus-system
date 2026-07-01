package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatPermissionMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JpaChatPermissionRepositoryAdapter implements IChatPermissionRepository {

	private final SpingDataChatPermissionRepository chatPermissionRepository;
	private final ChatPermissionMapper chatPermissionMapper;

	public JpaChatPermissionRepositoryAdapter(SpingDataChatPermissionRepository chatPermissionRepository,
			ChatPermissionMapper chatPermissionMapper) {
		this.chatPermissionRepository = chatPermissionRepository;
		this.chatPermissionMapper = chatPermissionMapper;
	}

	@Override
	public Set<ChatPermission> findAll() {
		return chatPermissionRepository.findAll().stream().map(chatPermissionMapper::toDomain)
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public void save(ChatPermission chatPermission) {
		chatPermissionRepository.save(chatPermissionMapper.toJpa(chatPermission));
	}

	@Override
	public Set<ChatPermission> findByCodes(Set<String> codes) {
		Set<ChatPermission> permissions = new HashSet<>();
		for (String code : codes) {
			ChatPermissionJpaEntity entity = chatPermissionRepository.findByCode(code);
			if (entity == null) {
				throw new IllegalStateException("Permission not found: " + code);
			}
			permissions.add(chatPermissionMapper.toDomain(entity));
		}
		return permissions;
	}

	@Override
	public Set<ChatPermission> findAllById(Set<UUID> ids) {
		Set<ChatPermission> permissions = new HashSet<>();
		for (UUID id : ids) {
			ChatPermissionJpaEntity entity = chatPermissionRepository.findById(id).orElse(null);
			if (entity == null) {
				throw new IllegalStateException("Permission not found: " + id);
			}
			permissions.add(chatPermissionMapper.toDomain(entity));
		}
		return permissions;
	}

	@Override
	public Set<ChatPermission> findPermissionsByChatRoomAndParticipant(ChatRoomId chatRoomId,
			ParticipantId participantId) {

		return chatPermissionRepository
				.findPermissionsByChatRoomAndParticipant(chatRoomId.value(), participantId.value()).stream()
				.map(chatPermissionMapper::toDomain).collect(Collectors.toSet());
	}
}
