package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.MessageDevicePayloadCommand;
import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.MessageDevicePayloadRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.SendMessageRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.SendMessageResponse;

import java.util.UUID;

public final class SendMessageWebMapper {

	private SendMessageWebMapper() {
	}

	public static SendMessageCommand toCommand(UUID chatRoomId, SendMessageRequest request, UUID senderUserId) {
		return new SendMessageCommand(chatRoomId, senderUserId, request.senderDeviceId(), request.replyToMessageId(),
				request.payloads().stream().map(SendMessageWebMapper::toPayloadCommand).toList());
	}

	public static SendMessageResponse toResponse() {
		return new SendMessageResponse(true);
	}

	private static MessageDevicePayloadCommand toPayloadCommand(MessageDevicePayloadRequest request) {
		return new MessageDevicePayloadCommand(request.targetDeviceId(), request.encryptedPayload());
	}
}
