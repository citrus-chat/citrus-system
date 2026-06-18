package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

public record UserResponse(String id, String username, String email, boolean active) {
}
