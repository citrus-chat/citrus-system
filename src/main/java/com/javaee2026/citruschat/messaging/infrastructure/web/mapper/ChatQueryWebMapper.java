package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;
import com.javaee2026.citruschat.messaging.application.results.ChatRoomSummaryResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatMessageResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatRoomSummaryResponse;

public final class ChatQueryWebMapper {

	private ChatQueryWebMapper() {
	}

	public static ChatRoomSummaryResponse toChatRoomSummaryResponse(ChatRoomSummaryResult result) {
		return new ChatRoomSummaryResponse(result.id(), result.name(), result.type().name(), result.createdAt(),
				result.updatedAt());
	}

	public static ChatMessageResponse toChatMessageResponse(ChatMessageResult result) {
		return new ChatMessageResponse(result.id(), result.chatRoomId(), result.senderDeviceId(),
				result.replyToMessageId(), result.createdAt(), result.editedAt(), result.deletedAt());
	}
}
