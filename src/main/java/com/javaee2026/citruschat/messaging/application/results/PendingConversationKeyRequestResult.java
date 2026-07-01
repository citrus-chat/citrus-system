package com.javaee2026.citruschat.messaging.application.results;

import java.util.UUID;

public record PendingConversationKeyRequestResult(UUID conversationId, UUID targetUserId, UUID targetDeviceId,
		String publicKey) {
}
