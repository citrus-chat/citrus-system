package com.javaee2026.citruschat.messaging.infrastructure.websocket.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChatMessageWsRequest(@NotNull UUID messageId, @NotNull UUID chatRoomId, @NotNull UUID senderDeviceId,
		UUID replyMessageId, // This can be null
		@NotNull Integer keyVersion, @NotBlank String iv, @NotBlank String ciphertext) {
}
