package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.messaging.application.commands.UploadConversationKeyCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IConversationKeyDistributionRepository;
import com.javaee2026.citruschat.messaging.application.results.UploadConversationKeyResult;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyDistribution;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UploadConversationKeyUseCase {

	private final IConversationKeyDistributionRepository conversationRepository;
	private final IChatRoomRepository chatRoomRepository;
	private final IUserDeviceRepository deviceRepository;

	public UploadConversationKeyUseCase(IConversationKeyDistributionRepository conversationRepository,
			IChatRoomRepository chatRoomRepository, IUserDeviceRepository deviceRepository) {
		this.conversationRepository = conversationRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.deviceRepository = deviceRepository;
	}

	public UploadConversationKeyResult execute(UploadConversationKeyCommand command) {

		Optional<ConversationKeyDistribution> existing = conversationRepository.findByConversationAndDeviceAndVersion(
				command.conversationId(), command.targetDeviceId(), command.keyVersion());

		ChatRoom chatRoom = chatRoomRepository.findById(command.conversationId())
				.orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

		boolean participant = chatRoom.getParticipants().stream()
				.anyMatch(p -> p.getUserId().equals(command.targetUserId()));

		if (!participant) {
			throw new IllegalArgumentException("Target user is not participant of conversation");
		}

		UserDevice device = deviceRepository.findActiveById(command.targetDeviceId().value())
				.orElseThrow(() -> new IllegalArgumentException("Device not found"));

		if (!device.getUserId().equals(command.targetUserId())) {
			throw new IllegalArgumentException("Device does not belong to target user");
		}

		if (command.keyVersion() < 1) {
			throw new IllegalArgumentException("Invalid key version");
		}

		ConversationKeyDistribution distribution = new ConversationKeyDistribution(UUID.randomUUID(),
				command.conversationId(), command.targetUserId(), command.targetDeviceId(), command.senderDeviceId(),
				command.keyVersion(), command.ciphertext(), command.iv(), Instant.now());

		ConversationKeyDistribution saved = conversationRepository.save(distribution);

		return new UploadConversationKeyResult(saved.getId());
	}
}
