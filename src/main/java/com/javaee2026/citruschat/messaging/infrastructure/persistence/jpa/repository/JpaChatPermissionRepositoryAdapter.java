package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatPermissionMapper;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.Set;

public class JpaChatPermissionRepositoryAdapter implements IChatPermissionRepository {

	private final SpingDataChatPermissionRepositoryAdapter chatPermissionRepository;
	private final ChatPermissionMapper chatPermissionMapper;

	public JpaChatPermissionRepositoryAdapter(SpingDataChatPermissionRepositoryAdapter chatPermissionRepository,
			ChatPermissionMapper chatPermissionMapper) {
		this.chatPermissionRepository = chatPermissionRepository;
		this.chatPermissionMapper = chatPermissionMapper;
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
			ChatPermission chatPermission = chatPermissionMapper.toDomain(entity);
			permissions.add(chatPermission);
		}
		return permissions;
	}
}
