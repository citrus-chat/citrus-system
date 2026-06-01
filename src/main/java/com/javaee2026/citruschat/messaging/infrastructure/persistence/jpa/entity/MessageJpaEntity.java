package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = TableNames.Messaging.MESSAGES)
@Getter
@Setter
public class MessageJpaEntity implements Persistable<UUID> {

	@Id
	private UUID id;

	@Column(name = "chat_room_id", nullable = false)
	private UUID chatRoomId;

	@Column(name = "sender_user_id", nullable = false)
	private UUID senderUserId;

	@Column(name = "sender_device_id", nullable = false)
	private UUID senderDeviceId;

	@Column(name = "reply_to_message_id")
	private UUID replyToMessageId;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "edited_at")
	private Instant editedAt;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@Transient
	private boolean isNew = true;

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	@PostLoad
	@PostPersist
	void markNotNew() {
		this.isNew = false;
	}
}
