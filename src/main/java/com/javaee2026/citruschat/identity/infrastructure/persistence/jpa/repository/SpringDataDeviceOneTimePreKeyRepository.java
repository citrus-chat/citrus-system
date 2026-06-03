package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceOneTimePreKeyJpaEntity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceOneTimePreKeyJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataDeviceOneTimePreKeyRepository extends JpaRepository<DeviceOneTimePreKeyJpaEntity, DeviceOneTimePreKeyJpaId> {

    List<DeviceOneTimePreKeyJpaEntity> findByIdDeviceId(UUID deviceId);

    Optional<DeviceOneTimePreKeyJpaEntity> findByIdDeviceIdAndIdKeyId(UUID deviceId, Integer keyId);

    Optional<DeviceOneTimePreKeyJpaEntity> findFirstByIdDeviceIdAndConsumedAtIsNull(UUID deviceId);

    long countByIdDeviceIdAndConsumedAtIsNull(UUID deviceId);

    void deleteByIdDeviceId(UUID deviceId);
}
