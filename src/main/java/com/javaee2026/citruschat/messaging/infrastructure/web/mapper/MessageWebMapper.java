package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.MessageResponse;

public final class MessageWebMapper {

	private MessageWebMapper() {
	}

	public static MessageResponse toResponse(Message message) {

		return new MessageResponse(message.getId().value(), message.getChatRoomId().value(),
				message.getSenderUserId().value(), message.getSenderDeviceId().value(),
				message.getReplyToMessageId() != null ? message.getReplyToMessageId().value() : null,
				message.getContent().getKeyVersion(), message.getContent().getIv(),
				message.getContent().getCiphertext(), message.getCreatedAt(), message.getEditedAt(),
				message.getDeletedAt());
	}
}
