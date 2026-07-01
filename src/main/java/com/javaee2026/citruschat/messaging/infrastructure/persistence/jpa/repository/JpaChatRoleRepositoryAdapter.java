package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatRoleRepository;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoleMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JpaChatRoleRepositoryAdapter implements IChatRoleRepository {

	private final SpringDataChatRoleRepository chatRoleRepository;
	private final SpringDataChatRoomRepository chatRoomRepository;
	private final SpingDataChatPermissionRepository chatPermissionRepository;
	private final ChatRoleMapper chatRoleMapper;

	public JpaChatRoleRepositoryAdapter(SpringDataChatRoleRepository chatRoleRepository,
			SpringDataChatRoomRepository chatRoomRepository, SpingDataChatPermissionRepository chatPermissionRepository,
			ChatRoleMapper chatRoleMapper) {
		this.chatRoleRepository = chatRoleRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.chatPermissionRepository = chatPermissionRepository;
		this.chatRoleMapper = chatRoleMapper;
	}

	@Override
	@Transactional
	public List<ChatRole> findByChatRoomId(ChatRoomId chatRoomId) {
		return chatRoleRepository.findByChatRoomId(chatRoomId.value()).stream().map(chatRoleMapper::toDomain).toList();
	}

	@Override
	@Transactional
	public Optional<ChatRole> findByIdAndChatRoomId(RoleId roleId, ChatRoomId chatRoomId) {
		return chatRoleRepository.findByIdAndChatRoomId(roleId.value(), chatRoomId.value())
				.map(chatRoleMapper::toDomain);
	}

	@Override
	@Transactional
	public ChatRole save(ChatRole role) {
		return persist(role);
	}

	@Override
	@Transactional
	public ChatRole update(ChatRole role) {
		return persist(role);
	}

	@Override
	@Transactional
	public boolean delete(RoleId roleId) {
		chatRoleRepository.deleteRolePermissionsByRoleId(roleId.value());
		chatRoleRepository.deleteParticipantRolesByRoleId(roleId.value());
		return chatRoleRepository.deleteExistingById(roleId.value()) > 0;
	}

	@Override
	public boolean existsByNameAndChatRoomId(String name, ChatRoomId chatRoomId) {
		return chatRoleRepository.existsByNameAndChatRoomId(name, chatRoomId.value());
	}

	@Override
	public boolean existsByNameAndChatRoomIdExcludingRole(String name, ChatRoomId chatRoomId, RoleId excludedRoleId) {
		return chatRoleRepository.existsByNameAndChatRoomIdExcludingRole(name, chatRoomId.value(),
				excludedRoleId.value());
	}

	@Override
	public boolean existsByIdAndChatRoomId(RoleId roleId, ChatRoomId chatRoomId) {
		return chatRoleRepository.findByIdAndChatRoomId(roleId.value(), chatRoomId.value()).isPresent();
	}

	private ChatRole persist(ChatRole role) {
		ChatRoomJpaEntity chatRoom = chatRoomRepository.findById(role.getChatRoomId().value())
				.orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

		Map<UUID, ChatPermissionJpaEntity> permissionsById = chatPermissionRepository
				.findAllById(role.getRolePermissions().stream().map(p -> p.getId().value()).toList()).stream()
				.collect(Collectors.toMap(ChatPermissionJpaEntity::getId, Function.identity()));

		return chatRoleMapper.toDomain(chatRoleRepository.save(ChatRoleMapper.toJpa(role, chatRoom, permissionsById)));
	}
}
