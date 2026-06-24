package com.javaee2026.citruschat.identity.application.results;

import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;

public record UserDeviceKeysResult(UserId userId, List<DeviceKeyResult> devices) {
}
