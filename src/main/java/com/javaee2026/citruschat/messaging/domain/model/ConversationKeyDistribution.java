package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ConversationKeyDistribution {

	private final UUID id;
	private final ChatRoomId conversationId;
	private final UserId targetUserId;
	private final DeviceId targetDeviceId;
	private final DeviceId senderDeviceId;
	private final Integer keyVersion;
	private final String ciphertext;
	private final String iv;
	private final Instant createdAt;

	public ConversationKeyDistribution(UUID id, ChatRoomId conversationId, UserId targetUserId, DeviceId targetDeviceId,
			DeviceId senderDeviceId, Integer keyVersion, String ciphertext, String iv, Instant createdAt) {

		this.id = id;
		this.conversationId = conversationId;
		this.targetUserId = targetUserId;
		this.targetDeviceId = targetDeviceId;
		this.senderDeviceId = senderDeviceId;
		this.keyVersion = keyVersion;
		this.ciphertext = ciphertext;
		this.iv = iv;
		this.createdAt = createdAt;
	}
}
