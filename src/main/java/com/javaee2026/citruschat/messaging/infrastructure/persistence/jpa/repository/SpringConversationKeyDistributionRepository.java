package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ConversationKeyDistributionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringConversationKeyDistributionRepository
		extends
			JpaRepository<ConversationKeyDistributionJpaEntity, UUID> {

	Optional<ConversationKeyDistributionJpaEntity> findByConversationIdAndTargetDeviceIdAndKeyVersion(
			UUID conversationId, UUID targetDeviceId, Integer keyVersion);

}
