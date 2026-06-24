package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IConversationKeyDistributionRepository;
import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyDistribution;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ConversationKeyDistributionJpaMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaConversationKeyDistributionRepositoryAdapter implements IConversationKeyDistributionRepository {

	private final SpringConversationKeyDistributionRepository repository;

	public JpaConversationKeyDistributionRepositoryAdapter(SpringConversationKeyDistributionRepository repository) {
		this.repository = repository;
	}

	@Override
	public ConversationKeyDistribution save(ConversationKeyDistribution distribution) {

		return ConversationKeyDistributionJpaMapper
				.toDomain(repository.save(ConversationKeyDistributionJpaMapper.toJpa(distribution)));
	}

	@Override
	public Optional<ConversationKeyDistribution> findByConversationAndDeviceAndVersion(ChatRoomId conversationId,
			DeviceId deviceId, Integer keyVersion) {
		return repository.findByConversationIdAndTargetDeviceIdAndKeyVersion(conversationId.value(), deviceId.value(),
				keyVersion).map(ConversationKeyDistributionJpaMapper::toDomain);
	}

	@Override
	public List<ConversationKeyDistribution> findByTargetDeviceAndCreatedAfter(DeviceId deviceId, Instant createdAt) {
		return repository.findByTargetDeviceIdAndCreatedAtAfterOrderByCreatedAtAsc(deviceId.value(), createdAt).stream()
				.map(ConversationKeyDistributionJpaMapper::toDomain).toList();
	}
}
