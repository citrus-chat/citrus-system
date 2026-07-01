package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateWebLoginTokenResponse(String token,

		@JsonProperty("web_device_id") String webDeviceId,

		@JsonProperty("expires_at") String expiresAt,

		@JsonProperty("qr_payload") String qrPayload) {
}
