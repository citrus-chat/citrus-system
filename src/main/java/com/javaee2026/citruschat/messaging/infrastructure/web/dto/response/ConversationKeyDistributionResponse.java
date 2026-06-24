package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.UUID;

// We don't need the commented sections.. for now
public record ConversationKeyDistributionResponse(
		// UUID id,
		UUID conversationId,
		// UUID targetUserId,
		// UUID targetDeviceId,
		UUID senderDeviceId, Integer keyVersion, String ciphertext, String iv, Instant createdAt) {
}
