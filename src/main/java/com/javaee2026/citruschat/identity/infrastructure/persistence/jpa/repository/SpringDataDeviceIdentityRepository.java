package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceIdentityJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataDeviceIdentityRepository extends JpaRepository<DeviceIdentityJpaEntity, UUID> {
}
