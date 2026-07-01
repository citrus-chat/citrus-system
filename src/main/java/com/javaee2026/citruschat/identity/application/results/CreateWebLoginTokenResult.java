package com.javaee2026.citruschat.identity.application.results;

import java.time.Instant;
import java.util.UUID;

public record CreateWebLoginTokenResult(String token, UUID webDeviceId, Instant expiresAt, String qrPayload) {
}
