package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.UUID;

//import com.javaee2026.citruschat.messaging.application.results.ChatRoomSummaryResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataChatRoomRepository extends JpaRepository<ChatRoomJpaEntity, UUID> {
	@Query("""
			SELECT DISTINCT cr
			FROM ChatRoomJpaEntity cr
			JOIN cr.participants p
			WHERE p.userId = :userId
			AND cr.deletedAt IS NULL
			""")
	List<ChatRoomJpaEntity> findAllChatRooms(UUID userId);

	@Query("""
			SELECT COUNT(DISTINCT cr) > 0
			FROM ChatRoomJpaEntity cr
			JOIN cr.participants p1
			JOIN cr.participants p2
			WHERE cr.type = :chatRoomType
			  AND cr.deletedAt IS NULL
			  AND p1.userId = :participant1
			  AND p2.userId = :participant2
			  AND p1 <> p2
			""")
	boolean existsDirectChatBetweenParticipants(UUID participant1, UUID participant2, ChatRoomType chatRoomType);

	// @Query("""
	// SELECT new
	// com.javaee2026.citruschat.messaging.application.results.ChatRoomSummaryResult(
	// cr.id,
	// cr.name,
	// cr.type,
	// cr.createdAt,
	// cr.updatedAt
	// )
	// FROM ChatRoomJpaEntity cr
	// JOIN ChatParticipantJpaEntity cp ON cp.chatRoom.id = cr.id
	// WHERE cp.userId = :userId
	// AND cp.leftAt IS NULL
	// ORDER BY cr.updatedAt DESC
	// """)
	// List<ChatRoomSummaryResult> findActiveChatRoomsByUserId(UUID userId);

	List<ChatRoomJpaEntity> findByCreatedBy(UUID createdBy);
}
