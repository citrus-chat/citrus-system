package com.javaee2026.citruschat.identity.application.commands;

import java.util.UUID;

public record UpdateUserProfileCommand(UUID userId, String username, String description, String privacy,
		boolean showPhone, boolean showEmail, boolean showStatus, boolean showDescription, boolean allowGroupInvites) {
}
