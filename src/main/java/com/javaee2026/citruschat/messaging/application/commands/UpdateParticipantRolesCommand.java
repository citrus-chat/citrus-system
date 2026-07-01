package com.javaee2026.citruschat.messaging.application.commands;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;

public record UpdateParticipantRolesCommand(ChatRoomId chatRoomId, ParticipantId participantId, UserId requesterUserId,
		List<RoleId> roleIds) {
}
