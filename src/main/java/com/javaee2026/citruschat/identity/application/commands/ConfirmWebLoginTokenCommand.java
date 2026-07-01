package com.javaee2026.citruschat.identity.application.commands;

import java.util.UUID;

public record ConfirmWebLoginTokenCommand(String token, UUID authenticatedUserId) {
}
