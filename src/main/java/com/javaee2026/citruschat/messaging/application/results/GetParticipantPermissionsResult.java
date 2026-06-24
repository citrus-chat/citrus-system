package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;

import java.util.Set;

public record GetParticipantPermissionsResult(Set<ChatPermission> permissions) {
}
