package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.application.results.ChatMessageResult;
import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;

import java.util.List;

public interface IMessageRepository {

	List<ChatMessageResult> findMessagesByChatRoomId(ChatRoomId chatRoomId, int page, int size);

	List<Message> findMessagesAfter(ChatRoomId chatRoomId, MessageId lastMessageId, int limit);

	boolean existsById(MessageId messageId);

	void save(Message message);

}
