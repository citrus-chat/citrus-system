package com.javaee2026.citruschat.identity.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
        String username,

        @Size(max = 500) String description,

        @NotNull @Pattern(regexp = "public|contacts|private") String privacy,

        boolean showPhone,
        boolean showEmail,
        boolean showStatus,
        boolean showDescription,
        boolean allowGroupInvites) {
}