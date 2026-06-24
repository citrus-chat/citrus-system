package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import java.util.UUID;

public record UserProfileResponse(UUID userId, String username, String avatarUrl,

		String description, String privacy, boolean showPhone, boolean showEmail, boolean showStatus,
		boolean showDescription, boolean allowGroupInvites) {
}
