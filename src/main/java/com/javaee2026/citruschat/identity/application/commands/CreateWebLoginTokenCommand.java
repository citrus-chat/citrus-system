package com.javaee2026.citruschat.identity.application.commands;

import java.util.UUID;

public record CreateWebLoginTokenCommand(UUID webDeviceId, String webDeviceName, String webPublicKey) {
}
