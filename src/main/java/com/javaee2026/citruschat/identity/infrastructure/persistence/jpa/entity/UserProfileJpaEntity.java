package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = TableNames.Identity.USER_PROFILES)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileJpaEntity {

	@Id
	@Column(name = "user_id")
	private UUID userId;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description = "";

	@Column(nullable = false, length = 20)
	private String privacy = "public";

	@Column(nullable = false)
	private boolean showPhone = true;

	@Column(nullable = false)
	private boolean showEmail = true;

	@Column(nullable = false)
	private boolean showStatus = true;

	@Column(nullable = false)
	private boolean showDescription = true;

	@Column(nullable = false)
	private boolean allowGroupInvites = true;

	@Column(nullable = false)
	private Instant updatedAt = Instant.now();
}
