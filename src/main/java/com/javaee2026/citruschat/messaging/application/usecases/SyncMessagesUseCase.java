package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.SyncMessagesCommand;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.SyncMessagesResult;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;

import java.time.Instant;

public class SyncMessagesUseCase {

	private final IMessageRepository messageRepository;
	private final ChatPermissionAuthorizationService permissionAuthorizationService;

	public SyncMessagesUseCase(IMessageRepository messageRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		this.messageRepository = messageRepository;
		this.permissionAuthorizationService = permissionAuthorizationService;
	}

	public SyncMessagesResult execute(SyncMessagesCommand command) {
		ChatRoomId chatRoomId = command.chatRoomId();
		Instant lastCreatedAt = command.lastCreatedAt();

		permissionAuthorizationService.requirePermission(chatRoomId, command.requesterUserId(),
				ChatPermissionList.CAN_VIEW_MESSAGE);

		return new SyncMessagesResult(messageRepository.findMessagesAfter(chatRoomId, lastCreatedAt, 100));
	}
}
