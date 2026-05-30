package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;

import java.util.List;
import java.util.UUID;

public class GetChatRoomMessagesUseCase {

	private final IMessageRepository messageRepository;
	private final ValidateChatParticipantUseCase validateChatParticipantUseCase;

	public GetChatRoomMessagesUseCase(IMessageRepository messageRepository,
			ValidateChatParticipantUseCase validateChatParticipantUseCase) {
		this.messageRepository = messageRepository;
		this.validateChatParticipantUseCase = validateChatParticipantUseCase;
	}

	public List<ChatMessageResult> execute(UUID chatRoomId, UUID userId, int page, int size) {
		if (chatRoomId == null || userId == null) {
			throw new IllegalArgumentException("chatRoomId and userId cannot be null");
		}

		if (!validateChatParticipantUseCase.execute(chatRoomId, userId)) {
			throw new IllegalArgumentException("User is not participant of this chat room");
		}

		int safePage = Math.max(page, 0);
		int safeSize = Math.min(Math.max(size, 1), 100);

		return messageRepository.findMessagesByChatRoomId(chatRoomId, safePage, safeSize);
	}
}
