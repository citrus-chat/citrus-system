package com.javaee2026.citruschat.messaging.application.results;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record CreateChatRoomResult(ChatRoomId id, ChatRoomType type, String name, UserId createdBy,
		List<ChatParticipant> participants, Map<String, ChatRole> roles, Instant createdAt, Instant updatedAt,
		Instant deletedAt) {
}
