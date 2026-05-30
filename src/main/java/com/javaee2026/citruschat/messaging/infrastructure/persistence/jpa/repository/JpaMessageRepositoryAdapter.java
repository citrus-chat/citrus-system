package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;
import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.domain.model.MessageDevicePayload;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.MessageDevicePayloadMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.MessageMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public class JpaMessageRepositoryAdapter implements IMessageRepository {

	private final SpringDataMessageRepository messageRepository;
	private final SpringDataMessageDevicePayloadRepository payloadRepository;
	private final MessageMapper messageMapper;

	public JpaMessageRepositoryAdapter(SpringDataMessageRepository messageRepository,
			SpringDataMessageDevicePayloadRepository payloadRepository, MessageMapper messageMapper) {
		this.messageRepository = messageRepository;
		this.payloadRepository = payloadRepository;
		this.messageMapper = messageMapper;
	}

	@Override
	public List<ChatMessageResult> findMessagesByChatRoomId(UUID chatRoomId, int page, int size) {
		return messageRepository.findMessagesByChatRoomId(chatRoomId, PageRequest.of(page, size));
	}
	@Override
	@Transactional
	public void save(Message message, List<MessageDevicePayload> payloads) {

		messageRepository.save(MessageMapper.toJpa(message));

		payloadRepository.saveAll(payloads.stream().map(MessageDevicePayloadMapper::toJpa).toList());
	}
}
