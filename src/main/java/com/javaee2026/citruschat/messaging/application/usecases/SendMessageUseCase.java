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

public class SendMessageUseCase {

	private final IUserRepository userRepository;
	private final IUserDeviceRepository deviceRepository;
	private final IChatRoomRepository chatRoomRepository;
	private final IMessageRepository messageRepository;
	private final MessageFactory messageFactory;
	private final IMessageRealtimeNotifier realtimeNotifier;
	private final ChatPermissionAuthorizationService permissionAuthorizationService;

	public SendMessageUseCase(IUserDeviceRepository deviceRepository, IUserRepository userRepository,
			IChatRoomRepository chatRoomRepository, IMessageRepository messageRepository, MessageFactory messageFactory,
			IMessageRealtimeNotifier realtimeNotifier,
			ChatPermissionAuthorizationService permissionAuthorizationService) {
		this.deviceRepository = deviceRepository;
		this.userRepository = userRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.messageRepository = messageRepository;
		this.messageFactory = messageFactory;
		this.realtimeNotifier = realtimeNotifier;
		this.permissionAuthorizationService = permissionAuthorizationService;
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
		permissionAuthorizationService.requirePermission(chatRoom, sender.getId(), ChatPermissionList.CAN_SEND_MESSAGE);
	}

	private void validateDevice(UserId senderUserId, DeviceId senderDeviceId) {
		deviceRepository.findActiveByIdAndUserId(senderDeviceId.value(), senderUserId.value()).orElseThrow(
				() -> new IllegalArgumentException("Sender device not found with id: " + senderDeviceId.value()));
	}

	@Transactional
	public void execute(SendMessageCommand command) {

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

		Message message = messageFactory.createNew(messageId, chatRoom.getId(), senderUserId, senderDeviceId,
				command.replyMessageId() != null ? command.replyMessageId() : null, content);

		messageRepository.save(message);

		realtimeNotifier.notifyMessageCreated(message.getChatRoomId());
	}
}
