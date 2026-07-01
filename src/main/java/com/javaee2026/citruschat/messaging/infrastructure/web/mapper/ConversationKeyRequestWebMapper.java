package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.results.PendingConversationKeyRequestResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.PendingConversationKeyRequestResponse;

import java.util.List;

public class ConversationKeyRequestWebMapper {

	public static PendingConversationKeyRequestResponse toResponse(PendingConversationKeyRequestResult result) {
		return new PendingConversationKeyRequestResponse(result.conversationId(), result.targetUserId(),
				result.targetDeviceId(), result.publicKey());
	}

	public static List<PendingConversationKeyRequestResponse> toResponse(
			List<PendingConversationKeyRequestResult> results) {
		return results.stream().map(ConversationKeyRequestWebMapper::toResponse).toList();
	}
}
