package com.javaee2026.citruschat.messaging.infrastructure.web.mapper;

import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyDistribution;
import com.javaee2026.citruschat.messaging.infrastructure.web.dto.response.ConversationKeyDistributionResponse;

public class ConversationKeyDistributionResponseMapper {

	private ConversationKeyDistributionResponseMapper() {
	}

	public static ConversationKeyDistributionResponse toResponse(ConversationKeyDistribution distribution) {
		// We don't need the commented section
		return new ConversationKeyDistributionResponse(
				// distribution.getId(),
				distribution.getConversationId().value(),
				// distribution.getTargetUserId().value(),
				// distribution.getTargetDeviceId().value(),
				distribution.getSenderDeviceId().value(), distribution.getKeyVersion(), distribution.getCiphertext(),
				distribution.getIv(), distribution.getCreatedAt());
	}
}
