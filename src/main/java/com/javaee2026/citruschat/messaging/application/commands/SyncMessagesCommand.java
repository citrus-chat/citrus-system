package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;

public record SyncMessagesCommand(ChatRoomId chatRoomId, UserId requesterUserId, Instant lastCreatedAt) {
}
