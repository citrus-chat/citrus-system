package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatParticipantJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataChatParticipantRepository extends JpaRepository<ChatParticipantJpaEntity, UUID> {
	List<ChatRoomId> findChatRoomIdsByUserId(UUID userId);
}
