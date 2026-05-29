package com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO para respuesta de mensaje WebSocket. Broadcast a
 * /topic/chatrooms/{chatRoomId}.
 */
public record ChatMessageWsResponse(@JsonProperty("id") UUID id,

		@JsonProperty("chatRoomId") UUID chatRoomId,

		@JsonProperty("senderUserId") UUID senderUserId,

		@JsonProperty("content") String content,

		@JsonProperty("sentAt") Instant sentAt) {
}
