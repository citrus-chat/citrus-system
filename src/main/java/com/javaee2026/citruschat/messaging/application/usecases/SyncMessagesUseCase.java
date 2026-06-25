package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.SyncMessagesCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.SyncMessagesResult;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;

import java.time.Instant;

public class SyncMessagesUseCase {

	private final IMessageRepository messageRepository;
	private final IChatRoomRepository chatRoomRepository;

	public SyncMessagesUseCase(IMessageRepository messageRepository, IChatRoomRepository chatRoomRepository) {
		this.messageRepository = messageRepository;
		this.chatRoomRepository = chatRoomRepository;
	}

	public SyncMessagesResult execute(SyncMessagesCommand command) {
		ChatRoomId chatRoomId = command.chatRoomId();
		Instant lastCreatedAt = command.lastCreatedAt();

		chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new IllegalArgumentException("Chat room not found with id: " + chatRoomId.value()));

		return new SyncMessagesResult(messageRepository.findMessagesAfter(chatRoomId, lastCreatedAt, 100));
	}
}
