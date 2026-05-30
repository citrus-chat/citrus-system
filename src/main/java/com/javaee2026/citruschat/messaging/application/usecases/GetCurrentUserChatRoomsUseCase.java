package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.results.ChatRoomSummaryResult;

import java.util.List;
import java.util.UUID;

public class GetCurrentUserChatRoomsUseCase {

	private final IChatRoomRepository chatRoomRepository;

	public GetCurrentUserChatRoomsUseCase(IChatRoomRepository chatRoomRepository) {
		this.chatRoomRepository = chatRoomRepository;
	}

	public List<ChatRoomSummaryResult> execute(UUID userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}

		return chatRoomRepository.findActiveChatRoomsByUserId(userId);
	}
}
