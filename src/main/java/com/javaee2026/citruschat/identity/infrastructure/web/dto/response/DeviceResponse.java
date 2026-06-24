package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import java.util.UUID;

public record DeviceResponse(UUID deviceId, String publicKey) {
}
