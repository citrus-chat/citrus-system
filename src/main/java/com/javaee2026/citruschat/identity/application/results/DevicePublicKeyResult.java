package com.javaee2026.citruschat.identity.application.results;

import java.util.UUID;

public record DevicePublicKeyResult(UUID deviceId, String publicKey) {
}
