package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ChatPermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpingDataChatPermissionRepositoryAdapter extends JpaRepository<ChatPermissionJpaEntity, UUID> {
	ChatPermissionJpaEntity findByCode(String code);
}
