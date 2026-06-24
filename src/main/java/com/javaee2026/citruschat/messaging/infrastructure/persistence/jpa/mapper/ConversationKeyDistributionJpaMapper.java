package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyDistribution;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity.ConversationKeyDistributionJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

public final class ConversationKeyDistributionJpaMapper {

	private ConversationKeyDistributionJpaMapper() {
	}

	public static ConversationKeyDistributionJpaEntity toJpa(ConversationKeyDistribution distribution) {

		ConversationKeyDistributionJpaEntity entity = new ConversationKeyDistributionJpaEntity();

		entity.setId(distribution.getId());
		entity.setConversationId(distribution.getConversationId().value());
		entity.setTargetUserId(distribution.getTargetUserId().value());
		entity.setTargetDeviceId(distribution.getTargetDeviceId().value());
		entity.setKeyVersion(distribution.getKeyVersion());
		entity.setCiphertext(distribution.getCiphertext());
		entity.setIv(distribution.getIv());
		entity.setCreatedAt(distribution.getCreatedAt());

		entity.markNew();

		return entity;
	}

	public static ConversationKeyDistribution toDomain(ConversationKeyDistributionJpaEntity entity) {

		return new ConversationKeyDistribution(entity.getId(), new ChatRoomId(entity.getConversationId()),
				new UserId(entity.getTargetUserId()), new DeviceId(entity.getTargetDeviceId()), entity.getKeyVersion(),
				entity.getCiphertext(), entity.getIv(), entity.getCreatedAt());
	}
}
