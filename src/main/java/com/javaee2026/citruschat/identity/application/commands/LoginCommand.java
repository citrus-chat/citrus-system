package com.javaee2026.citruschat.identity.application.commands;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;

import java.util.UUID;

public record LoginCommand(String email, String password, UUID deviceId, String deviceName, DeviceType deviceType) {
}
