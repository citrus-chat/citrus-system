package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatParticipantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataChatParticipantRepository extends JpaRepository<ChatParticipantJpaEntity, UUID> {
	boolean existsByChatRoomIdAndIdAndLeftAtIsNull(UUID chatRoomId, UUID id);

	Optional<ChatParticipantJpaEntity> findByChatRoomIdAndIdAndLeftAtIsNull(UUID chatRoomId, UUID id);

	Optional<ChatParticipantJpaEntity> findByChatRoomIdAndUserIdAndLeftAtIsNull(UUID chatRoomId, UUID userId);

	@Query("""
			SELECT COUNT(cp)
			FROM ChatParticipantJpaEntity cp
			JOIN cp.roles r
			WHERE cp.chatRoom.id = :chatRoomId
			  AND cp.leftAt IS NULL
			  AND r.id = :roleId
			""")
	long countActiveParticipantsUsingRole(@Param("chatRoomId") UUID chatRoomId, @Param("roleId") UUID roleId);

	@Query("""
			SELECT DISTINCT cp
			FROM ChatParticipantJpaEntity cp
			JOIN cp.roles r
			WHERE cp.chatRoom.id = :chatRoomId
			  AND cp.leftAt IS NULL
			  AND r.id = :roleId
			""")
	List<ChatParticipantJpaEntity> findActiveByChatRoomIdAndRoleId(@Param("chatRoomId") UUID chatRoomId,
			@Param("roleId") UUID roleId);

	@Query("""
			SELECT COUNT(DISTINCT cp) > 0
			FROM ChatParticipantJpaEntity cp
			JOIN cp.roles r
			WHERE cp.chatRoom.id = :chatRoomId
			  AND cp.leftAt IS NULL
			  AND r.id IN :roleIds
			""")
	boolean existsActiveParticipantWithAnyRole(@Param("chatRoomId") UUID chatRoomId,
			@Param("roleIds") List<UUID> roleIds);
}
