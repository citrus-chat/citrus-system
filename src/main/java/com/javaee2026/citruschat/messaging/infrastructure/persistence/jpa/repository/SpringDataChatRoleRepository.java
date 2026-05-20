package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataChatRoleRepository extends JpaRepository<ChatRoleJpaEntity, UUID> {
	List<ChatRoleJpaEntity> findByChatRoomIdIsNull();
}
