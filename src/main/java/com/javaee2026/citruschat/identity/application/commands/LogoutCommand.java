package com.javaee2026.citruschat.identity.application.commands;

import java.util.UUID;

public record LogoutCommand(UUID deviceId, UUID userId) {
}
