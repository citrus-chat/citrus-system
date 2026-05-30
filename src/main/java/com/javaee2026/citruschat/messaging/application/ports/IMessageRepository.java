package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;
import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.domain.model.MessageDevicePayload;

import java.util.List;
import java.util.UUID;

public interface IMessageRepository {
	List<ChatMessageResult> findMessagesByChatRoomId(UUID chatRoomId, int page, int size);
	void save(Message message, List<MessageDevicePayload> payloads);
}
