package com.javaee2026.citruschat.identity.application.commands;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;

import java.util.UUID;

public record RegisterOrRefreshUserDeviceCommand(UUID deviceId, UUID userId, String publicKey, String deviceName,
		DeviceType deviceType) {
}
