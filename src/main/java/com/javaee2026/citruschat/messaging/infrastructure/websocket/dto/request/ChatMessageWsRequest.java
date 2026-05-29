package com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.request;

import java.util.List;
import java.util.UUID;

public record ChatMessageWsRequest(UUID chatRoomId, UUID senderDeviceId, UUID replyToMessageId,
		List<ChatMessageDevicePayloadWsRequest> payloads) {
}
