package com.javaee2026.citruschat.messaging.infrastructure.websocket.mapper;

import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.application.results.SendMessageResult;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.request.ChatMessageWsRequest;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.response.ChatMessageWsResponse;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;
import java.util.UUID;

public final class ChatWebSocketMapper {

	private ChatWebSocketMapper() {
	}

	public static SendMessageCommand toCommand(ChatMessageWsRequest request, UUID senderId) {
		return new SendMessageCommand(new UserId(senderId), new MessageId(request.messageId()),
				new ChatRoomId(request.chatRoomId()), new DeviceId(request.senderDeviceId()),
				new MessageId(request.replyMessageId()), request.keyVersion(), request.iv(), request.ciphertext());
	}

	public static ChatMessageWsResponse fromResult(SendMessageResult result, UUID senderUserId) {
		return new ChatMessageWsResponse(result.message().getId().value(), result.message().getChatRoomId().value(),
				senderUserId, null,
				result.message().getCreatedAt() != null ? result.message().getCreatedAt() : Instant.now());
	}
}
