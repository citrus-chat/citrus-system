package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;

import java.time.Instant;
import java.util.UUID;

public record ChatRoomSummaryResult(UUID id, String name, ChatRoomType type, Instant createdAt, Instant updatedAt) {
}
