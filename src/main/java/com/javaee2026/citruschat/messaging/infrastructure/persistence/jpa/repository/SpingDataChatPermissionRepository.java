package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface SpingDataChatPermissionRepository extends JpaRepository<ChatPermissionJpaEntity, UUID> {
	ChatPermissionJpaEntity findByCode(String code);

	@Query("""
			    SELECT DISTINCT p
			    FROM ChatParticipantJpaEntity cp
			        JOIN cp.roles r
			        JOIN r.permissions p
			    WHERE cp.chatRoom.id = :chatRoomId
			      AND cp.id = :participantId
			""")
	Set<ChatPermissionJpaEntity> findPermissionsByChatRoomAndParticipant(@Param("chatRoomId") UUID chatRoomId,
			@Param("participantId") UUID participantId);
}
