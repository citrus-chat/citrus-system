package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.CreateChatRoomCommand;
import com.javaee2026.citruschat.messaging.application.results.CreateChatRoomResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.CreateChatRoomRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatParticipantResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoleResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.CreateChatRoomResponse;

import java.util.*;
import java.util.stream.Collectors;

public final class CreateChatRoomWebMapper {
	private CreateChatRoomWebMapper() {
	}

	public static CreateChatRoomCommand toCommand(CreateChatRoomRequest request, UUID creatorId) {
		return new CreateChatRoomCommand(request.chatRoomType(), request.name(), creatorId, request.participantIds());
	}

	public static CreateChatRoomResponse toResponse(CreateChatRoomResult result) {

		List<ChatParticipantResponse> participants = result.participants().stream()
				.map(ChatParticipantResponseMapper::toResponse).toList();

		Map<String, ChatRoleResponse> roles = result.roles().entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, entry -> ChatRoleResponseMapper.toResponse(entry.getValue())));

		return new CreateChatRoomResponse(result.id().value(), result.type().toString(), result.name(),
				result.createdBy().value(), result.createdAt(), result.updatedAt(), result.deletedAt());
	}
}
