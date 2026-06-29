package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;
import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.MessageMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public class JpaMessageRepositoryAdapter implements IMessageRepository {

	private final SpringDataMessageRepository messageRepository;
	private final MessageMapper messageMapper;

	public JpaMessageRepositoryAdapter(SpringDataMessageRepository messageRepository, MessageMapper messageMapper) {
		this.messageRepository = messageRepository;
		this.messageMapper = messageMapper;
	}

	@Override
	public List<ChatMessageResult> findMessagesByChatRoomId(ChatRoomId chatRoomId, int page, int size) {
		return messageRepository.findMessagesByChatRoomId(chatRoomId.value(), PageRequest.of(page, size));
	}

	@Override
	public List<Message> findMessagesAfter(ChatRoomId chatRoomId, Instant lastCreatedAt, int limit) {
		Pageable pageable = PageRequest.of(0, limit);

		if (lastCreatedAt == null) {
			return messageRepository.findByChatRoomIdAndDeletedAtIsNullOrderByCreatedAtAsc(chatRoomId.value(), pageable)
					.stream().map(messageMapper::toDomain).toList();
		}

		return messageRepository
				.findByChatRoomIdAndCreatedAtGreaterThanAndDeletedAtIsNullOrderByCreatedAtAsc(chatRoomId.value(),
						lastCreatedAt, pageable)
				.stream().map(messageMapper::toDomain).toList();
	}

	@Override
	public boolean existsById(MessageId messageId) {
		return messageRepository.existsById(messageId.value());
	}

	@Override
	public void save(Message message) {
		messageRepository.save(MessageMapper.toJpa(message));
	}
}
