package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DeviceOneTimePreKeyJpaId implements Serializable {

	@Column(name = "device_id", nullable = false)
	private UUID deviceId;

	@Column(name = "key_id", nullable = false)
	private Integer keyId;
}
