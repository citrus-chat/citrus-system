package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ConversationKeyRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataConversationKeyRequestRepository
		extends
			JpaRepository<ConversationKeyRequestJpaEntity, UUID> {

	boolean existsByConversationIdAndTargetDeviceId(UUID conversationId, UUID targetDeviceId);

	List<ConversationKeyRequestJpaEntity> findAllByTargetDeviceId(UUID targetDeviceId);

	void deleteByConversationIdAndTargetDeviceId(UUID conversationId, UUID targetDeviceId);

}
