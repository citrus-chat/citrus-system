package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(UUID id, UUID chatRoomId, UUID senderUserId, UUID senderDeviceId, UUID replyToMessageId,
		Integer keyVersion, String iv, String ciphertext, Instant createdAt, Instant editedAt, Instant deletedAt) {
}
