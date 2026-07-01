package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfirmWebLoginTokenResponse(@JsonProperty("user_id") String userId,

		@JsonProperty("web_device_id") String webDeviceId) {
}
