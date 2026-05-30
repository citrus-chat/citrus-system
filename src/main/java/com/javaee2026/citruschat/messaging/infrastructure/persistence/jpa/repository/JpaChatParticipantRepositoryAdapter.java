package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;

import java.util.UUID;

public class JpaChatParticipantRepositoryAdapter implements IChatParticipantRepository {

	private final SpringDataChatParticipantRepository chatParticipantRepository;

	public JpaChatParticipantRepositoryAdapter(SpringDataChatParticipantRepository chatParticipantRepository) {
		this.chatParticipantRepository = chatParticipantRepository;
	}
	@Override
	public boolean existsActiveByChatRoomIdAndUserId(UUID chatRoomId, UUID userId) {
		return chatParticipantRepository.existsByChatRoomIdAndUserIdAndLeftAtIsNull(chatRoomId, userId);
	}
}
