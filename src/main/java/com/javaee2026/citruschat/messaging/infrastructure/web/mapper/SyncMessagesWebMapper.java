package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.results.SyncMessagesResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.MessageResponse;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.SyncMessagesResponse;

import java.util.List;

public final class SyncMessagesWebMapper {

	private SyncMessagesWebMapper() {
	}

	public static SyncMessagesResponse toResponse(SyncMessagesResult result) {

		List<MessageResponse> messages = result.messages().stream().map(MessageWebMapper::toResponse).toList();

		return new SyncMessagesResponse(messages);
	}
}
