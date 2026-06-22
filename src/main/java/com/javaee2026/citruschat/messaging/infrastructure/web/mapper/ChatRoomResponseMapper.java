package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoomResponse;

public final class ChatRoomResponseMapper {

	private ChatRoomResponseMapper() {
	}

	public static ChatRoomResponse toResponse(ChatRoom chatRoom) {

		return new ChatRoomResponse(chatRoom.getId().value(), chatRoom.getType().toString(), chatRoom.getName(),
				chatRoom.getCreatedBy().value(), chatRoom.getCreatedAt(), chatRoom.getUpdatedAt(),
				chatRoom.getDeletedAt());
	}
}
