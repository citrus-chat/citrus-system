package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatRoomJpaEntity;

public interface SpringDataChatRoomRepository extends JpaRepository<ChatRoomJpaEntity, UUID> {
	List<ChatRoomJpaEntity> findByCreatedBy(UUID createdBy);
}
