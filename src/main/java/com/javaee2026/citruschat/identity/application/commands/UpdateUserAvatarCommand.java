package com.javaee2026.citruschat.identity.application.commands;

import java.util.UUID;

public record UpdateUserAvatarCommand(UUID userId, String originalFilename, String contentType, byte[] content) {
}
