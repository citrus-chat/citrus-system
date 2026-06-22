package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

public record SyncChatRoomCommand(DeviceId deviceId) {
}
