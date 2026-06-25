package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.domain.factory.MessageFactory;
import com.javaee2026.citruschat.messaging.domain.model.*;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.messaging.domain.valueobjects.EncryptedContent;
import com.javaee2026.citruschat.messaging.infrastructure.websocket.ports.IMessageRealtimeNotifier;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class SendMessageUseCase {

	private final IUserRepository userRepository;
	private final IUserDeviceRepository deviceRepository;
	private final IChatRoomRepository chatRoomRepository;
	private final IMessageRepository messageRepository;
	private final MessageFactory messageFactory;
	private final IMessageRealtimeNotifier realtimeNotifier;

	public SendMessageUseCase(IUserDeviceRepository deviceRepository, IUserRepository userRepository,
			IChatRoomRepository chatRoomRepository, IMessageRepository messageRepository, MessageFactory messageFactory,
			IMessageRealtimeNotifier realtimeNotifier) {
		this.deviceRepository = deviceRepository;
		this.userRepository = userRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.messageRepository = messageRepository;
		this.messageFactory = messageFactory;
		this.realtimeNotifier = realtimeNotifier;
	}

	private User validateSender(UserId userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId.value()));
	}

	private ChatRoom validateChatRoom(ChatRoomId chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new IllegalArgumentException("Chat room not found with id: " + chatRoomId.value()));
	}

	public void validateSenderPermissions(ChatRoom chatRoom, User sender) {
		List<ChatParticipant> participants = new ArrayList<>(chatRoom.getParticipants());
		if (participants.isEmpty()) {
			throw new IllegalArgumentException("Chat room has no participants");
		}

		// Find Sender
		ChatParticipant senderParticipant = participants.stream()
				.filter(participant -> participant.getUserId().equals(sender.getId())).findFirst()
				.orElseThrow(() -> new IllegalArgumentException(
						"Sender '" + sender.getId().value() + "' is not a participant of the chat room"));

		// Validates that the user HAS PERMISSION TO SEND MESSAGE
		if (!chatRoom.hasPermission(senderParticipant, ChatPermissionList.CAN_SEND_MESSAGE)) {
			throw new IllegalArgumentException("Sender '" + sender.getId().value()
					+ "' does not have permission to send messages in this chat room");
		}
	}

	private void validateDevice(UserId senderUserId, DeviceId senderDeviceId) {
		deviceRepository.findActiveByIdAndUserId(senderDeviceId.value(), senderUserId.value()).orElseThrow(
				() -> new IllegalArgumentException("Sender device not found with id: " + senderDeviceId.value()));
	}

	@Transactional
	public void execute(SendMessageCommand command) {

		System.out.println("Command: " + command);

		UserId senderUserId = command.senderUserId();

		DeviceId senderDeviceId = command.senderDeviceId();

		User user = validateSender(senderUserId);

		validateDevice(senderUserId, senderDeviceId);

		ChatRoom chatRoom = validateChatRoom(command.chatRoomId());

		validateSenderPermissions(chatRoom, user);

		MessageId messageId = command.messageId();

		if (messageRepository.existsById(messageId)) {
			return;
		}

		EncryptedContent content = new EncryptedContent(command.keyVersion(), command.iv(), command.ciphertext());

		System.out.println("Content creado: " + content);

		Message message = messageFactory.createNew(messageId, chatRoom.getId(), senderUserId, senderDeviceId,
				command.replyMessageId() != null ? command.replyMessageId() : null, content);

		System.out.println("Mensaje creado: " + message);

		messageRepository.save(message);

		realtimeNotifier.notifyMessageCreated(message.getChatRoomId());
	}
}
