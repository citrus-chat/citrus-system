package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageResponse(UUID id, UUID chatRoomId, UUID senderDeviceId, UUID replyToMessageId,
		Instant createdAt, Instant editedAt, Instant deletedAt) {
}
