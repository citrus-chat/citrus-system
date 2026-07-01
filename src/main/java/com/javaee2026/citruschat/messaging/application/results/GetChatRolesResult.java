package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.model.ChatRole;

import java.util.List;

public record GetChatRolesResult(List<ChatRole> roles) {
}
