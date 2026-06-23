package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.SendMessageRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.SendMessageResponse;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public final class SendMessageWebMapper {

	private SendMessageWebMapper() {
	}

	public static SendMessageCommand toCommand(SendMessageRequest request, UUID senderId) {

		return new SendMessageCommand(new UserId(senderId), new MessageId(request.messageId()),
				new ChatRoomId(request.chatRoomId()), new DeviceId(request.senderDeviceId()),
				request.replyMessageId() != null ? new MessageId(request.replyMessageId()) : null, request.keyVersion(),
				request.iv(), request.ciphertext());
	}

	public static SendMessageResponse toResponse() {
		return new SendMessageResponse(true);
	}
}
