package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;

import java.util.Set;
import java.util.UUID;

public interface IChatPermissionRepository {
	Set<ChatPermission> findByCodes(Set<String> codes);
	Set<ChatPermission> findAllById(Set<UUID> ids);

	Set<ChatPermission> findPermissionsByChatRoomAndParticipant(ChatRoomId chatRoomId, ParticipantId participantId);

	void save(ChatPermission chatPermission);
}
