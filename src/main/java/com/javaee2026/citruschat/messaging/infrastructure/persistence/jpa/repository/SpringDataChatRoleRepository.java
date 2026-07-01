package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataChatRoleRepository extends JpaRepository<ChatRoleJpaEntity, UUID> {
	List<ChatRoleJpaEntity> findByChatRoomIdIsNull();

	@Query("""
			SELECT r
			FROM ChatRoleJpaEntity r
			WHERE r.chatRoom.id = :chatRoomId
			""")
	List<ChatRoleJpaEntity> findByChatRoomId(@Param("chatRoomId") UUID chatRoomId);

	@Query("""
			SELECT r
			FROM ChatRoleJpaEntity r
			WHERE r.id = :roleId
			  AND r.chatRoom.id = :chatRoomId
			""")
	Optional<ChatRoleJpaEntity> findByIdAndChatRoomId(@Param("roleId") UUID roleId,
			@Param("chatRoomId") UUID chatRoomId);

	@Query("""
			SELECT COUNT(r) > 0
			FROM ChatRoleJpaEntity r
			WHERE r.chatRoom.id = :chatRoomId
			  AND LOWER(r.name) = LOWER(:name)
			""")
	boolean existsByNameAndChatRoomId(@Param("name") String name, @Param("chatRoomId") UUID chatRoomId);

	@Query("""
			SELECT COUNT(r) > 0
			FROM ChatRoleJpaEntity r
			WHERE r.chatRoom.id = :chatRoomId
			  AND LOWER(r.name) = LOWER(:name)
			  AND r.id <> :excludedRoleId
			""")
	boolean existsByNameAndChatRoomIdExcludingRole(@Param("name") String name, @Param("chatRoomId") UUID chatRoomId,
			@Param("excludedRoleId") UUID excludedRoleId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "DELETE FROM chat_role_permissions WHERE role_id = :roleId", nativeQuery = true)
	void deleteRolePermissionsByRoleId(@Param("roleId") UUID roleId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "DELETE FROM chat_participant_roles WHERE role_id = :roleId", nativeQuery = true)
	void deleteParticipantRolesByRoleId(@Param("roleId") UUID roleId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
			DELETE FROM ChatRoleJpaEntity r
			WHERE r.id = :roleId
			""")
	int deleteExistingById(@Param("roleId") UUID roleId);
}
