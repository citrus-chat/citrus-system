package com.javaee2026.citruschat.messaging.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendMessageRequest(@NotNull UUID messageId, @NotNull UUID chatRoomId, @NotNull UUID senderDeviceId,
		UUID replyMessageId, // This can be null
		@NotNull Integer keyVersion, @NotBlank String iv, @NotBlank String ciphertext) {
}
