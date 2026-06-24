package com.javaee2026.citruschat.messaging.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UploadConversationKeyRequest(

		@NotNull UUID conversationId,

		@NotNull UUID targetUserId,

		@NotNull UUID targetDeviceId,

		@NotNull Integer keyVersion,

		@NotBlank String ciphertext,

		@NotBlank String iv) {
}
