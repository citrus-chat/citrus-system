package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.ports.IConversationKeyRequestRepository;
import com.javaee2026.citruschat.messaging.application.results.PendingConversationKeyRequestResult;
import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyRequest;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.ports.IMessageRealtimeNotifier;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GetConversationKeysRequestsUseCase {

	private final IConversationKeyRequestRepository repository;
	private final IMessageRealtimeNotifier notifier;

	public GetConversationKeysRequestsUseCase(IConversationKeyRequestRepository repository,
			IMessageRealtimeNotifier notifier) {
		this.repository = repository;
		this.notifier = notifier;
	}

	@Transactional
	public List<PendingConversationKeyRequestResult> execute(UUID conversationId) {

		ChatRoomId chatRoomId = new ChatRoomId(conversationId);

		List<ConversationKeyRequest> entities = repository.findAllByConversationId(chatRoomId);

		List<PendingConversationKeyRequestResult> results = new ArrayList<>();

		for (ConversationKeyRequest request : entities) {
			results.add(new PendingConversationKeyRequestResult(request.conversationId().value(),
					request.targetUserId().value(), request.targetDeviceId().value(), request.targetPublicKey()));

			repository.deleteByConversationIdAndTargetDeviceId(chatRoomId, request.targetDeviceId());
		}

		notifier.notifyKeysUpdated(chatRoomId);

		return results;
	}
}
