package com.javaee2026.citruschat.messaging.application.ports;

import com.javaee2026.citruschat.messaging.domain.model.ConversationKeyRequest;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;

import java.util.List;

public interface IConversationKeyRequestRepository {

	ConversationKeyRequest save(ConversationKeyRequest request);

	boolean existsByConversationIdAndTargetDeviceId(ChatRoomId conversationId, DeviceId targetDeviceId);

	List<ConversationKeyRequest> findAllByConversationId(ChatRoomId conversationId);

	void deleteByConversationIdAndTargetDeviceId(ChatRoomId conversationId, DeviceId targetDeviceId);

}
