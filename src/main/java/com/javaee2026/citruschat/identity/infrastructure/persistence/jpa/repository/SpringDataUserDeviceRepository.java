package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserDeviceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserDeviceRepository extends JpaRepository<UserDeviceJpaEntity, UUID> {
	Optional<UserDeviceJpaEntity> findByIdAndUserIdAndRevokedAtIsNull(UUID id, UUID userId);

	Optional<UserDeviceJpaEntity> findByUserIdAndDeviceTypeAndRevokedAtIsNull(UUID userId, DeviceType deviceType);

	Optional<UserDeviceJpaEntity> findByIdAndRevokedAtIsNull(UUID id);

	List<UserDeviceJpaEntity> findAllByUserIdAndRevokedAtIsNullOrderByLastSeenDescCreatedAtDesc(UUID userId);

	List<UserDeviceJpaEntity> findUserDeviceJpaEntitiesByUserId(UUID userId);

	boolean existsByUserIdAndRevokedAtIsNull(UUID userId);

	boolean existsByIdAndUserIdAndRevokedAtIsNull(UUID id, UUID userId);
}
