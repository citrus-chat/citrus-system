package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatParticipantJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatParticipantMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JpaChatParticipantRepositoryAdapter implements IChatParticipantRepository {

	private final SpringDataChatParticipantRepository chatParticipantRepository;
	private final SpringDataChatRoleRepository chatRoleRepository;

	public JpaChatParticipantRepositoryAdapter(SpringDataChatParticipantRepository chatParticipantRepository,
			SpringDataChatRoleRepository chatRoleRepository) {
		this.chatParticipantRepository = chatParticipantRepository;
		this.chatRoleRepository = chatRoleRepository;
	}

	@Override
	public boolean existsActiveByChatRoomIdAndParticipantId(UUID chatRoomId, UUID participantId) {
		return chatParticipantRepository.existsByChatRoomIdAndIdAndLeftAtIsNull(chatRoomId, participantId);
	}

	@Override
	public boolean existsChatParticipantByChatRoomIdAndUserId(UUID chatRoomId, UUID userId) {
		return chatParticipantRepository.existsByChatRoomIdAndUserId(chatRoomId, userId);
	}

	@Override
	@Transactional
	public Optional<ChatParticipant> findActiveByChatRoomIdAndParticipantId(ChatRoomId chatRoomId,
			ParticipantId participantId) {
		return chatParticipantRepository.findByChatRoomIdAndIdAndLeftAtIsNull(chatRoomId.value(), participantId.value())
				.map(ChatParticipantMapper::toDomain);
	}

	@Override
	@Transactional
	public Optional<ChatParticipant> findActiveByChatRoomIdAndUserId(ChatRoomId chatRoomId, UserId userId) {
		return chatParticipantRepository.findByChatRoomIdAndUserIdAndLeftAtIsNull(chatRoomId.value(), userId.value())
				.map(ChatParticipantMapper::toDomain);
	}

	@Override
	public boolean isRoleAssignedToAnyParticipant(ChatRoomId chatRoomId, RoleId roleId) {
		return countActiveParticipantsUsingRole(chatRoomId, roleId) > 0;
	}

	@Override
	@Transactional
	public void replaceRoleForParticipants(ChatRoomId chatRoomId, RoleId oldRoleId, RoleId replacementRoleId) {
		ChatRoleJpaEntity replacementRole = chatRoleRepository.findById(replacementRoleId.value())
				.orElseThrow(() -> new IllegalArgumentException("Replacement role not found"));

		if (!replacementRole.getChatRoom().getId().equals(chatRoomId.value())) {
			throw new IllegalArgumentException("Replacement role does not belong to this chat room");
		}

		List<ChatParticipantJpaEntity> participants = chatParticipantRepository
				.findActiveByChatRoomIdAndRoleId(chatRoomId.value(), oldRoleId.value());

		for (ChatParticipantJpaEntity participant : participants) {
			participant.getRoles().removeIf(role -> role.getId().equals(oldRoleId.value()));
			boolean alreadyHasReplacement = participant.getRoles().stream()
					.anyMatch(role -> role.getId().equals(replacementRoleId.value()));
			if (!alreadyHasReplacement) {
				participant.getRoles().add(replacementRole);
			}
		}

		chatParticipantRepository.saveAll(participants);
	}

	@Override
	public boolean existsActiveParticipantWithAnyRole(ChatRoomId chatRoomId, List<RoleId> roleIds) {
		if (roleIds == null || roleIds.isEmpty()) {
			return false;
		}
		return chatParticipantRepository.existsActiveParticipantWithAnyRole(chatRoomId.value(),
				roleIds.stream().map(RoleId::value).toList());
	}

	@Override
	public long countActiveParticipantsUsingRole(ChatRoomId chatRoomId, RoleId roleId) {
		return chatParticipantRepository.countActiveParticipantsUsingRole(chatRoomId.value(), roleId.value());
	}

	@Override
	@Transactional
	public void replaceParticipantRoles(ChatRoomId chatRoomId, ParticipantId participantId, List<RoleId> roleIds) {
		ChatParticipantJpaEntity participant = chatParticipantRepository
				.findByChatRoomIdAndIdAndLeftAtIsNull(chatRoomId.value(), participantId.value())
				.orElseThrow(() -> new IllegalArgumentException("Active chat participant not found"));

		List<UUID> requestedRoleIds = roleIds.stream().map(RoleId::value).distinct().toList();
		Map<UUID, ChatRoleJpaEntity> rolesById = chatRoleRepository.findAllById(requestedRoleIds).stream()
				.collect(Collectors.toMap(ChatRoleJpaEntity::getId, Function.identity()));

		if (rolesById.size() != requestedRoleIds.size()) {
			throw new IllegalArgumentException("Role not found");
		}

		List<ChatRoleJpaEntity> roles = requestedRoleIds.stream().map(rolesById::get).toList();
		boolean foreignRole = roles.stream()
				.anyMatch(role -> !role.getChatRoom().getId().equals(participant.getChatRoom().getId()));

		if (foreignRole) {
			throw new IllegalArgumentException("Role does not belong to this chat room");
		}

		participant.getRoles().clear();
		participant.getRoles().addAll(roles);
		chatParticipantRepository.save(participant);
	}
}
