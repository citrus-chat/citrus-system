package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyDistribution;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IConversationKeyDistributionRepository {

	ConversationKeyDistribution save(ConversationKeyDistribution distribution);

	Optional<ConversationKeyDistribution> findByConversationAndDeviceAndVersion(ChatRoomId conversationId,
			DeviceId deviceId, Integer keyVersion);

	List<ConversationKeyDistribution> findByTargetDeviceAndCreatedAfter(DeviceId deviceId, Instant createdAt);
}
