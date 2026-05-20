package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_participants")
@Getter
@Setter
@NoArgsConstructor
public class ChatParticipantJpaEntity {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoomJpaEntity chatRoom;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@ManyToMany
	@JoinTable(name = "chat_participant_roles", joinColumns = @JoinColumn(name = "participant_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<ChatRoleJpaEntity> roles;

	@Column(name = "joined_at", nullable = false)
	private Instant joinedAt;

	private Instant leftAt;

	@Column(name = "last_read_message_id")
	private UUID lastReadMessageId;
}
