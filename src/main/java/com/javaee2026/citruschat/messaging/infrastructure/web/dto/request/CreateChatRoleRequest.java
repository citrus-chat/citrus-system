package com.javaee2026.citruschat.messaging.infrastructure.web.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateChatRoleRequest(@NotBlank @Size(min = 2, max = 50) String name,
		@NotNull @Min(0) @Max(100) Integer priority, @NotEmpty List<@NotNull UUID> permissionIds) {
}
