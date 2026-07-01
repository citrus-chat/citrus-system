package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.util.UUID;

public record PendingConversationKeyRequestResponse(UUID conversationId, UUID targetUserId, UUID targetDeviceId,
		String publicKey) {
}
