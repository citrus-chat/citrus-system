package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.model.Message;

import java.util.List;

public record SyncMessagesResult(List<Message> messages) {
}
