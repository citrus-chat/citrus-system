package com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "chat_roles")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoleJpaEntity {

	@Id
	@Column(nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoomJpaEntity chatRoom;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "chat_role_permissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<ChatPermissionJpaEntity> permissions = new HashSet<>();

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer priority;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
}
