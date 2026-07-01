package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;
import java.util.Set;

public record UpdateParticipantRolesResult(ParticipantId participantId, ChatRoomId chatRoomId, UserId userId,
		List<RoleId> roleIds, Set<ChatPermission> permissions) {
}
