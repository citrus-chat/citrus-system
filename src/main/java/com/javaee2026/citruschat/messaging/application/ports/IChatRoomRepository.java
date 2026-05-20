package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.util.List;

public interface IChatRoomRepository {

	public List<ChatRoom> findChatRoomsCreatedBy(UserId creator);

	void save(ChatRoom chatRoom);
}
