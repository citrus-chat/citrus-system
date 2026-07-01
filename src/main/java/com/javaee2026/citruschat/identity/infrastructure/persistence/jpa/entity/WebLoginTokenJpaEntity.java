package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import com.javaee2026.citruschat.identity.domain.model.WebLoginTokenStatus;
import com.javaee2026.citruschat.shared.infrastructure.persistence.constants.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = TableNames.Identity.WEB_LOGIN_TOKENS, indexes = {
		@Index(name = "idx_web_login_tokens_token_hash", columnList = "token_hash", unique = true)})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebLoginTokenJpaEntity {

	@Id
	private UUID id;

	@Column(name = "token_hash", nullable = false, unique = true, length = 64)
	private String tokenHash;

	@Column(name = "web_device_id", nullable = false)
	private UUID webDeviceId;

	@Column(name = "web_device_name", nullable = false)
	private String webDeviceName;

	@Column(name = "web_public_key", nullable = false, length = 512)
	private String webPublicKey;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private WebLoginTokenStatus status;

	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;

	@Column(name = "used_at")
	private Instant usedAt;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
}
