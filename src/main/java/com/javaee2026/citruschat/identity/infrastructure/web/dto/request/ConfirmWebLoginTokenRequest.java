package com.javaee2026.citruschat.identity.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConfirmWebLoginTokenRequest(@NotBlank(message = "Token is required") String token) {
}
