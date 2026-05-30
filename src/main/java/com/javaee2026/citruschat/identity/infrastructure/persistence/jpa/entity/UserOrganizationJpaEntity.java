package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = TableNames.Identity.USER_ORGANIZATION)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrganizationJpaEntity {

	@Id
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position_id", nullable = false)
	private PositionJpaEntity position;

	@Column(name = "manager_id")
	private UUID managerId;

	@Column(name = "assigned_at")
	private LocalDateTime assignedAt;
}
