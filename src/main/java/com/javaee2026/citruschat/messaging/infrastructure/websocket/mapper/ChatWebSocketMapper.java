package com.javaee2026.citruschat.messaging.infrastructure.websocket.mapper;

import com.javaee2026.citruschat.messaging.application.commands.MessageDevicePayloadCommand;
import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.application.results.SendMessageResult;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.request.ChatMessageWsRequest;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.response.ChatMessageWsResponse;

import java.time.Instant;
import java.util.UUID;

public final class ChatWebSocketMapper {

	private ChatWebSocketMapper() {
	}

	public static SendMessageCommand toCommand(ChatMessageWsRequest request, UUID senderUserId) {
		return new SendMessageCommand(request.chatRoomId(), senderUserId, request.senderDeviceId(),
				request.replyToMessageId(),
				request.payloads().stream().map(payload -> new MessageDevicePayloadCommand(payload.targetDeviceId(),
						payload.encryptedPayload())).toList());
	}

	public static ChatMessageWsResponse fromResult(SendMessageResult result, UUID senderUserId) {
		return new ChatMessageWsResponse(result.message().getId().value(), result.message().getChatRoomId().value(),
				senderUserId, null,
				result.message().getCreatedAt() != null ? result.message().getCreatedAt() : Instant.now());
	}
}
