package com.javaee2026.citruschat.identity.application.commands;

import com.javaee2026.citruschat.identity.application.dto.DeviceInfo;

public record LoginCommand(String email, String password, DeviceInfo deviceInfo) {
}
