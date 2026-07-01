package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.commands.UpdateParticipantRolesCommand;
import com.javaee2026.citruschat.messaging.application.results.UpdateParticipantRolesResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.request.UpdateParticipantRolesRequest;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.UpdateParticipantRolesResponse;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public final class UpdateParticipantRolesWebMapper {

	private UpdateParticipantRolesWebMapper() {
	}

	public static UpdateParticipantRolesCommand toCommand(UpdateParticipantRolesRequest request, UUID chatRoomId,
			UUID participantId, UUID requesterUserId) {
		return new UpdateParticipantRolesCommand(new ChatRoomId(chatRoomId), new ParticipantId(participantId),
				new UserId(requesterUserId), request.roleIds().stream().map(RoleId::new).toList());
	}

	public static UpdateParticipantRolesResponse toResponse(UpdateParticipantRolesResult result) {
		return new UpdateParticipantRolesResponse(result.participantId().value(), result.chatRoomId().value(),
				result.userId().value(), result.roleIds().stream().map(RoleId::value).toList(),
				result.permissions().stream().map(ChatPermissionResponseMapper::toResponse).toList());
	}
}
