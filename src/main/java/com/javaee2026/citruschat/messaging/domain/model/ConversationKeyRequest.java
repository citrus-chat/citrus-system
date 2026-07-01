package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;
import java.util.UUID;

public record ConversationKeyRequest(UUID id, ChatRoomId conversationId, UserId targetUserId, DeviceId targetDeviceId,
		String targetPublicKey, Instant createdAt) {
}
