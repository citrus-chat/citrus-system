package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.application.results.ChatRoomSummaryResult;
import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;
import java.util.UUID;

public interface IChatRoomRepository {

	public List<ChatRoom> findAllChatRooms(UserId user);
	public List<ChatRoom> findChatRoomsCreatedBy(UserId user);

	public Boolean existsDirectChatBetweenParticipants(UserId participant1, UserId participant2);
	List<ChatRoomSummaryResult> findActiveChatRoomsByUserId(UUID userId);
	void save(ChatRoom chatRoom);
}
