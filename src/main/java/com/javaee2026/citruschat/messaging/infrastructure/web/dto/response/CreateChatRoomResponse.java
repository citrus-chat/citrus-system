package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record CreateChatRoomResponse(UUID id, String name, String type, Instant createdAt, Instant updatedAt) {
}
