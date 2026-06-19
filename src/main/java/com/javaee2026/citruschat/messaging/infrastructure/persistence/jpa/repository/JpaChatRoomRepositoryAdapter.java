package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
//import com.javaee2026.citruschat.messaging.application.results.ChatRoomSummaryResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoomMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

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
	public List<ChatRoom> findAllChatRooms(UserId user) {
		return chatRoomRepository.findAllChatRooms(user.value()).stream().map(chatRoomMapper::toDomain).toList();
	}

	@Override
	public List<ChatRoom> findChatRoomsCreatedBy(UserId user) {
		return chatRoomRepository.findByCreatedBy(user.value()).stream().map(chatRoomMapper::toDomain).toList();
	}

	@Override
	public Boolean existsDirectChatBetweenParticipants(UserId participant1, UserId participant2) {
		return chatRoomRepository.existsDirectChatBetweenParticipants(participant1.value(), participant2.value(),
				ChatRoomType.DIRECT);
	}

	// @Override
	// public List<ChatRoomSummaryResult> findActiveChatRoomsByUserId(UUID userId) {
	// return chatRoomRepository.findActiveChatRoomsByUserId(userId);
	// }

	@Override
	public Optional<ChatRoom> findById(ChatRoomId id) {
		return chatRoomRepository.findById(id.value()).map(chatRoomMapper::toDomain);
	}
}
