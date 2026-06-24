package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.application.results.GetParticipantPermissionsResult;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ParticipantPermissionsResponse;

public final class GetParticipantPermissionsWebMapper {

	private GetParticipantPermissionsWebMapper() {
	}

	public static ParticipantPermissionsResponse toResponse(GetParticipantPermissionsResult result) {

		return new ParticipantPermissionsResponse(
				result.permissions().stream().map(ChatPermissionResponseMapper::toResponse).toList());
	}

}
