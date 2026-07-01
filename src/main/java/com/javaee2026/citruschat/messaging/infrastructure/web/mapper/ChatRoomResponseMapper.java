package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoleResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoomResponse;

import java.util.Map;
import java.util.stream.Collectors;

public final class ChatRoomResponseMapper {

	private ChatRoomResponseMapper() {
	}

	public static ChatRoomResponse toResponse(ChatRoom chatRoom) {
		Map<String, ChatRoleResponse> roles = chatRoom.getType() == ChatRoomType.GROUP
				? chatRoom.getRoles().entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey,
								entry -> ChatRoleResponseMapper.toResponse(entry.getValue())))
				: Map.of();

		return new ChatRoomResponse(chatRoom.getId().value(), chatRoom.getType().toString(), chatRoom.getName(),
				chatRoom.getCreatedBy().value(),
				chatRoom.getParticipants().stream().map(ChatParticipantResponseMapper::toResponse).toList(), roles,
				chatRoom.getCreatedAt(), chatRoom.getUpdatedAt(), chatRoom.getDeletedAt());
	}
}
