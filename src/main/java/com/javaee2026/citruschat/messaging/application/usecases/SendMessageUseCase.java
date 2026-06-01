package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.messaging.application.commands.MessageDevicePayloadCommand;
import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.SendMessageResult;
import com.javaee2026.citruschat.messaging.domain.factory.MessageDevicePayloadFactory;
import com.javaee2026.citruschat.messaging.domain.factory.MessageFactory;
import com.javaee2026.citruschat.messaging.domain.model.*;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendMessageUseCase {

	private final IUserDeviceRepository deviceRepository;
	private final IUserRepository userRepository;
	private final IChatRoomRepository chatRoomRepository;
	private final IMessageRepository messageRepository;
	private final MessageFactory messageFactory;
	private final MessageDevicePayloadFactory payloadFactory;

	public SendMessageUseCase(IUserDeviceRepository deviceRepository, IUserRepository userRepository,
			IChatRoomRepository chatRoomRepository, IMessageRepository messageRepository, MessageFactory messageFactory,
			MessageDevicePayloadFactory payloadFactory) {
		this.deviceRepository = deviceRepository;
		this.userRepository = userRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.messageRepository = messageRepository;
		this.messageFactory = messageFactory;
		this.payloadFactory = payloadFactory;

	}

	public User validateSender(UUID userId) {
		return userRepository.findById(new UserId(userId))
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	}

	public ChatRoom validateChatRoom(UUID chatRoomId) {
		return chatRoomRepository.findById(new ChatRoomId(chatRoomId))
				.orElseThrow(() -> new IllegalArgumentException("Chat room not found with id: " + chatRoomId));
	}

	public void validateSender(ChatRoom chatRoom, User sender) {
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
		} ;
	}

	public void validateDevice(UserId senderId, DeviceId senderDeviceId) {
		deviceRepository.findActiveByIdAndUserId(senderDeviceId.value(), senderId.value()).orElseThrow(
				() -> new IllegalArgumentException("Sender device not found with id: " + senderDeviceId.value()));
	}

	public List<DeviceId> getValidDevices(ChatRoom chatRoom, DeviceId senderDeviceId) {
		return chatRoom.getParticipants().stream()
				.flatMap(participant -> deviceRepository.findAllByUserId(participant.getUserId().value()).stream())
				.map(UserDevice::getId).filter(deviceId -> !deviceId.equals(senderDeviceId)) // Removemos el dispositivo
																								// emisor
				.toList();
	}

	public void validateTargetDevice(List<DeviceId> validDevices, List<MessageDevicePayload> messageDevices) {
		for (MessageDevicePayload messageDevicePayload : messageDevices) {
			if (messageDevicePayload.getTargetDeviceId() == null
					|| !validDevices.contains(messageDevicePayload.getTargetDeviceId())) {
				throw new IllegalArgumentException(
						"Target device id is not valid" + messageDevicePayload.getTargetDeviceId());
			}
		}
	}

	public SendMessageResult execute(SendMessageCommand command) {

		User sender = validateSender(command.senderUserId());

		ChatRoom chatRoom = validateChatRoom(command.chatRoomId());

		validateSender(chatRoom, sender); // Validates that the sender is a participant of the chat room and has
											// permission to send messages

		DeviceId deviceId = new DeviceId(command.senderDeviceId());

		validateDevice(sender.getId(), deviceId);

		List<DeviceId> validDevices = getValidDevices(chatRoom, deviceId);

		Message message = messageFactory.createNew(chatRoom.getId(), sender.getId(), deviceId,
				command.replyToMessageId() != null ? new MessageId(command.replyToMessageId()) : null);

		List<MessageDevicePayload> payloads = command.payloads().stream()
				.map(payload -> createPayload(message, payload)).toList();

		validateTargetDevice(validDevices, payloads);

		messageRepository.save(message, payloads);

		return new SendMessageResult(message, payloads);
	}

	private MessageDevicePayload createPayload(Message message, MessageDevicePayloadCommand payload) {
		return payloadFactory.create(message.getId(), new DeviceId(payload.targetDeviceId()),
				payload.encryptedPayload());
	}
}
