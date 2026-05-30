package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.MessageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface SpringDataMessageRepository extends JpaRepository<MessageJpaEntity, UUID> {
	@Query("""
			SELECT new com.javaee2026.citruschat.messaging.application.results.ChatMessageResult(
			    m.id,
			    m.chatRoomId,
			    m.senderDeviceId,
			    m.replyToMessageId,
			    m.createdAt,
			    m.editedAt,
			    m.deletedAt
			)
			FROM MessageJpaEntity m
			WHERE m.chatRoomId = :chatRoomId
			  AND m.deletedAt IS NULL
			ORDER BY m.createdAt DESC
			""")
	List<ChatMessageResult> findMessagesByChatRoomId(UUID chatRoomId, Pageable pageable);
}
