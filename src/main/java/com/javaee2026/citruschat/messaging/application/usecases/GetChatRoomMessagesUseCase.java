package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;
import java.util.UUID;

public class GetChatRoomMessagesUseCase {

	private final IMessageRepository messageRepository;
	private final ChatPermissionAuthorizationService permissionAuthorizationService;

	public GetChatRoomMessagesUseCase(IMessageRepository messageRepository,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		this.messageRepository = messageRepository;
		this.permissionAuthorizationService = permissionAuthorizationService;
	}

	public List<ChatMessageResult> execute(UUID chatRoomId, UUID userId, int page, int size) {
		if (chatRoomId == null || userId == null) {
			throw new IllegalArgumentException("chatRoomId and userId cannot be null");
		}

		int safePage = Math.max(page, 0);
		int safeSize = Math.min(Math.max(size, 1), 100);
		ChatRoomId roomId = new ChatRoomId(chatRoomId);

		permissionAuthorizationService.requirePermission(roomId, new UserId(userId),
				ChatPermissionList.CAN_VIEW_MESSAGE);

		return messageRepository.findMessagesByChatRoomId(roomId, safePage, safeSize);
	}
}
