package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = TableNames.Identity.USERS)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJpaEntity {

	@Id
	private UUID id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String passwordHash;

	@Column(nullable = true)
	private Instant validatedAt;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	@Column(nullable = true)
	private Instant deletedAt;

}
