package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AvatarResponse(@JsonProperty("avatar_url") String avatarUrl) {
}
