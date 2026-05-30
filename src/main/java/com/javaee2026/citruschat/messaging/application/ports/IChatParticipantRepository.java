package com.javaee2026.citruschat.messaging.application.ports;

import java.util.UUID;

public interface IChatParticipantRepository {
	boolean existsActiveByChatRoomIdAndUserId(UUID chatRoomId, UUID userId);
}
