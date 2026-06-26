package com.javaee2026.citruschat.messaging.application.ports;

import java.util.UUID;

public interface IChatParticipantRepository {
	boolean existsActiveByChatRoomIdAndParticipantId(UUID chatRoomId, UUID participantId);
}
