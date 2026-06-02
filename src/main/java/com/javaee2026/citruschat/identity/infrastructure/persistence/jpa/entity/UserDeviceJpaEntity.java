package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = TableNames.Identity.USER_DEVICES)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceJpaEntity {

	@Id
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "device_name")
	private String deviceName;

	@Enumerated(EnumType.STRING)
	@Column(name = "device_type", nullable = false)
	private DeviceType deviceType;

	@Column(name = "last_seen", nullable = false)
	private Instant lastSeen;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "revoked_at")
	private Instant revokedAt;
}
