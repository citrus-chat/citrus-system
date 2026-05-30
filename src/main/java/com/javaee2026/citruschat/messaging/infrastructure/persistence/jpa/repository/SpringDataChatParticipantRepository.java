package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatParticipantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataChatParticipantRepository extends JpaRepository<ChatParticipantJpaEntity, UUID> {
	boolean existsByChatRoomIdAndUserIdAndLeftAtIsNull(UUID chatRoomId, UUID userId);
}
