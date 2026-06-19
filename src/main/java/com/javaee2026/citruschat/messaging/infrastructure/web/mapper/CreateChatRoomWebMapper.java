package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.results.CreateChatRoomResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.CreateChatRoomResponse;

import java.util.UUID;

public final class CreateChatRoomWebMapper {
	private CreateChatRoomWebMapper() {
	}

	public static CreateChatRoomCommand toCommand(CreateChatRoomRequest request, UUID creatorId) {
		return new CreateChatRoomCommand(request.chatRoomType(), request.name(), creatorId, request.participantIds());
	}

	public static CreateChatRoomResponse toResponse(CreateChatRoomResult result) {
		return new CreateChatRoomResponse(result.id(), result.name(), result.type().toString(), result.createdAt(),
				result.updatedAt());
	}
}
