package com.javaee2026.citruschat.messaging.domain.model;

import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;

import java.time.Instant;
import java.util.UUID;

public class ConversationKeyDistribution {

	private final UUID id;
	private final ChatRoomId conversationId;
	private final UserId targetUserId;
	private final DeviceId targetDeviceId;
	private final Integer keyVersion;
	private final String ciphertext;
	private final String iv;
	private final Instant createdAt;

	public ConversationKeyDistribution(UUID id, ChatRoomId conversationId, UserId targetUserId, DeviceId targetDeviceId,
			Integer keyVersion, String ciphertext, String iv, Instant createdAt) {

		this.id = id;
		this.conversationId = conversationId;
		this.targetUserId = targetUserId;
		this.targetDeviceId = targetDeviceId;
		this.keyVersion = keyVersion;
		this.ciphertext = ciphertext;
		this.iv = iv;
		this.createdAt = createdAt;
	}

	public UUID getId() {
		return id;
	}

	public ChatRoomId getConversationId() {
		return conversationId;
	}

	public UserId getTargetUserId() {
		return targetUserId;
	}

	public DeviceId getTargetDeviceId() {
		return targetDeviceId;
	}

	public Integer getKeyVersion() {
		return keyVersion;
	}

	public String getCiphertext() {
		return ciphertext;
	}

	public String getIv() {
		return iv;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
