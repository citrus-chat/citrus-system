package com.javaee2026.citruschat.messaging.domain.factory;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChatRoomFactory {

	public ChatRoom createNew(ChatRoomType type, String name, UserId createdBy) {
		return new ChatRoom(ChatRoomId.newId(), type, name, createdBy, new ArrayList<>(), new HashMap<>(),
				Instant.now(), null, null);
	}

	public ChatRoom reconstitute(ChatRoomId id, ChatRoomType type, String name, UserId createdBy,
			List<ChatParticipant> participants, Map<String, ChatRole> roles, Instant createdAt, Instant updatedAt,
			Instant deletedAt) {
		return new ChatRoom(id, type, name, createdBy, participants, roles, createdAt, updatedAt, deletedAt);
	}
}
