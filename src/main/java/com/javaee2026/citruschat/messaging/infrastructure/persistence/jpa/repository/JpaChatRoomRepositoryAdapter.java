package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoomMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import jakarta.transaction.Transactional;

import java.util.List;

public class JpaChatRoomRepositoryAdapter implements IChatRoomRepository {

	private final SpringDataChatRoomRepository chatRoomRepository;
	private final ChatRoomMapper chatRoomMapper;

	public JpaChatRoomRepositoryAdapter(SpringDataChatRoomRepository chatRoomRepository,
			ChatRoomMapper chatRoomMapper) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatRoomMapper = chatRoomMapper;
	}

	@Override
	@Transactional
	public void save(ChatRoom chatRoom) {
		chatRoomRepository.save(ChatRoomMapper.toJpa(chatRoom));
	}

	@Override
	public List<ChatRoom> findChatRoomsCreatedBy(UserId creator) {
		return chatRoomRepository.findByCreatedBy(creator.value()).stream().map(chatRoomMapper::toDomain).toList();
	}
}
