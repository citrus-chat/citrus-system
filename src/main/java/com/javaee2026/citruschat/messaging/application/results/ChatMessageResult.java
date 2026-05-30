package com.javaee2026.citruschat.messaging.application.results;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageResult(UUID id, UUID chatRoomId, UUID senderDeviceId, UUID replyToMessageId, Instant createdAt,
		Instant editedAt, Instant deletedAt) {
}
