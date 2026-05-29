package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;

public interface IChatRoomRepository {

	public List<ChatRoom> findAllChatRooms(UserId user);
	public List<ChatRoom> findChatRoomsCreatedBy(UserId user);

	public Boolean existsDirectChatBetweenParticipants(UserId participant1, UserId participant2);

	void save(ChatRoom chatRoom);
}
