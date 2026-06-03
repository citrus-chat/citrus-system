package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceSignedPreKeyJpaEntity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceSignedPreKeyJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataDeviceSignedPreKeyRepository
		extends
			JpaRepository<DeviceSignedPreKeyJpaEntity, DeviceSignedPreKeyJpaId> {

	List<DeviceSignedPreKeyJpaEntity> findByIdDeviceId(UUID deviceId);

	Optional<DeviceSignedPreKeyJpaEntity> findByIdDeviceIdAndIdKeyId(UUID deviceId, Integer keyId);

	void deleteByIdDeviceId(UUID deviceId);

	Optional<DeviceSignedPreKeyJpaEntity> findFirstByIdDeviceIdAndExpiresAtAfterOrderByCreatedAtDesc(UUID deviceId,
			Instant now);
}
