package com.javaee2026.citruschat.messaging.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UpdateChatRoomResponse(UUID id, String name, String avatarUrl, Instant updatedAt) {
}
