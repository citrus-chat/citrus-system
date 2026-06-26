package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;

import java.time.Instant;

public record UpdateChatRoomResult(ChatRoomId id, String name, String avatarUrl, Instant updatedAt) {
}