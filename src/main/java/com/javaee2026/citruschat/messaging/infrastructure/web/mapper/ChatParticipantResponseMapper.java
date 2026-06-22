package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ChatParticipantResponse;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;

import java.util.Optional;

public final class ChatParticipantResponseMapper {
	public ChatParticipantResponseMapper() {
	}

	public static ChatParticipantResponse toResponse(ChatParticipant p) {
		return new ChatParticipantResponse(p.getId().value(), p.getChatRoomId().value(), p.getUserId().value(),
				p.getRoles().stream().map(RoleId::value).toList(), p.getJoinedAt(), p.getLeftAt(),
				Optional.ofNullable(p.getLastReadMessageId()).map(MessageId::value).orElse(null));
	}
}
