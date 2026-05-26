package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = TableNames.Messaging.CHAT_ROOMS)
public class ChatRoomJpaEntity {

	@Id
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChatRoomType type;

	private String name;

	@Column(name = "avatar_url")
	private String avatarUrl;

	@Column(name = "created_by", nullable = false)
	private UUID createdBy;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatRoleJpaEntity> roles = new ArrayList<>();

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatParticipantJpaEntity> participants = new ArrayList<>();

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	private Instant updatedAt;
	private Instant deletedAt;
}
