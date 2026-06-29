package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;

public record GetParticipantPermissionsCommand(ChatRoomId chatRoomId, ParticipantId participantId) {
}
