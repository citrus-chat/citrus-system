package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;

import java.util.UUID;

public class JpaChatParticipantRepositoryAdapter implements IChatParticipantRepository {

	private final SpringDataChatParticipantRepository chatParticipantRepository;

	public JpaChatParticipantRepositoryAdapter(SpringDataChatParticipantRepository chatParticipantRepository) {
		this.chatParticipantRepository = chatParticipantRepository;
	}
	@Override
	public boolean existsActiveByChatRoomIdAndParticipantId(UUID chatRoomId, UUID participantId) {
		return chatParticipantRepository.existsByChatRoomIdAndIdAndLeftAtIsNull(chatRoomId, participantId);
	}
}
