package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;

import java.util.UUID;

public class ValidateChatParticipantUseCase {

	private final IChatParticipantRepository chatParticipantRepository;

	public ValidateChatParticipantUseCase(IChatParticipantRepository chatParticipantRepository) {
		this.chatParticipantRepository = chatParticipantRepository;
	}

	public boolean execute(UUID chatRoomId, UUID participantId) {
		if (chatRoomId == null || participantId == null) {
			return false;
		}

		return chatParticipantRepository.existsActiveByChatRoomIdAndParticipantId(chatRoomId, participantId);
	}
}
