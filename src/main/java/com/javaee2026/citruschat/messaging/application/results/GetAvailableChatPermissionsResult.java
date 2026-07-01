package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;

import java.util.List;

public record GetAvailableChatPermissionsResult(List<ChatPermission> permissions) {
}
