package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = TableNames.Identity.POSITIONS)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionJpaEntity {

	@Id
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(name = "department_id")
	private UUID departmentId;

	@Column(name = "hierarchy_level")
	private Integer hierarchyLevel;

	@Column(name = "created_at")
	private Instant createdAt;
}
