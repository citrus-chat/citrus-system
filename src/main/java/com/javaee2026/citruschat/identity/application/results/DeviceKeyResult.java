package com.javaee2026.citruschat.identity.application.results;

import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

public record DeviceKeyResult(DeviceId deviceId, String publicKey) {
}
