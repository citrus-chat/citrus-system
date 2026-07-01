package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.RequestConversationKeyCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IConversationKeyRequestRepository;
import com.javaee2026.citruschat.messaging.domain.factory.ConversationKeyRequestFactory;
import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyRequest;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.UUID;

public class RequestConversationKeyUseCase {

	private final IConversationKeyRequestRepository keyRepository;
	private final IChatParticipantRepository participantRepository;
	private final IChatRoomRepository chatRoomRepository;

	public RequestConversationKeyUseCase(IConversationKeyRequestRepository keyRepository,
			IChatParticipantRepository participantRepository, IChatRoomRepository chatRoomRepository) {
		this.keyRepository = keyRepository;
		this.participantRepository = participantRepository;
		this.chatRoomRepository = chatRoomRepository;
	}

	public void execute(RequestConversationKeyCommand command, UUID userId) {

		chatRoomRepository.findById(command.conversationId())
				.orElseThrow(() -> new IllegalArgumentException("Conversation does not exist"));

		if (!participantRepository.existsChatParticipantByChatRoomIdAndUserId(command.conversationId().value(),
				userId)) {
			throw new IllegalStateException("Chat participant does not exist for the given conversation and user");
		}

		UserId requesterUserId = new UserId(userId);

		ConversationKeyRequest request = ConversationKeyRequestFactory.create(command.conversationId(), requesterUserId,
				command.requesterDeviceId(), command.requesterPublicKey().value());

		if (keyRepository.existsByConversationIdAndTargetDeviceId(command.conversationId(),
				command.requesterDeviceId())) {
			return;
		}

		keyRepository.save(request);
	}
}
