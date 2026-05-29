package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.messaging.application.commands.MessageDevicePayloadCommand;
import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.SendMessageResult;
import com.javaee2026.citruschat.messaging.domain.factory.MessageDevicePayloadFactory;
import com.javaee2026.citruschat.messaging.domain.factory.MessageFactory;
import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.domain.model.MessageDevicePayload;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;

import java.util.List;

public class SendMessageUseCase {

	private final IMessageRepository messageRepository;
	private final MessageFactory messageFactory;
	private final MessageDevicePayloadFactory payloadFactory;

	public SendMessageUseCase(IMessageRepository messageRepository, MessageFactory messageFactory,
			MessageDevicePayloadFactory payloadFactory) {
		this.messageRepository = messageRepository;
		this.messageFactory = messageFactory;
		this.payloadFactory = payloadFactory;
	}

	public SendMessageResult execute(SendMessageCommand command) {
		Message message = messageFactory.createNew(new ChatRoomId(command.chatRoomId()),
				new DeviceId(command.senderDeviceId()),
				command.replyToMessageId() != null ? new MessageId(command.replyToMessageId()) : null);

		List<MessageDevicePayload> payloads = command.payloads().stream()
				.map(payload -> createPayload(message, payload)).toList();

		messageRepository.save(message, payloads);

		return new SendMessageResult(message, payloads);
	}

	private MessageDevicePayload createPayload(Message message, MessageDevicePayloadCommand payload) {
		return payloadFactory.create(message.getId(), new DeviceId(payload.targetDeviceId()),
				payload.encryptedPayload());
	}
}
