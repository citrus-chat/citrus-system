package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public interface IChatParticipantRepository {
	boolean existsDirectChat(UserId user1, UserId user2);
	void save(ChatParticipant chatParticipant);
}
