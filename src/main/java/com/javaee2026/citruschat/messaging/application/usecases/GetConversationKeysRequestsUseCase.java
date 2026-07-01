package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.ports.IConversationKeyRequestRepository;
import com.javaee2026.citruschat.messaging.application.results.PendingConversationKeyRequestResult;
import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyRequest;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GetConversationKeysRequestsUseCase {

	private final IConversationKeyRequestRepository repository;

	public GetConversationKeysRequestsUseCase(IConversationKeyRequestRepository repository) {
		this.repository = repository;
	}

	public List<PendingConversationKeyRequestResult> execute(UUID conversationId) {

		List<ConversationKeyRequest> entities = repository.findAllByConversationId(new ChatRoomId(conversationId));

		List<PendingConversationKeyRequestResult> results = new ArrayList<>();

		for (ConversationKeyRequest request : entities) {
			results.add(new PendingConversationKeyRequestResult(request.conversationId().value(),
					request.targetUserId().value(), request.targetDeviceId().value(), request.targetPublicKey()));
		}

		return results;
	}
}
