package com.javaee2026.citruschat.identity.application.results;

import java.util.UUID;

public record UserProfileResult(
        UUID userId,
        String username,
        String avatarUrl,

        String description,
        String privacy,
        boolean showPhone,
        boolean showEmail,
        boolean showStatus,
        boolean showDescription,
        boolean allowGroupInvites) {
}