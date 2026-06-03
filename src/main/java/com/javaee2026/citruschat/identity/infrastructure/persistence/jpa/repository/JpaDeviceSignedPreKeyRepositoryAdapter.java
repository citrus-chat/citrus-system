package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IDeviceSignedPreKeyRepository;
import com.javaee2026.citruschat.identity.domain.model.DeviceSignedPreKey;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper.DeviceSignedPreKeyMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaDeviceSignedPreKeyRepositoryAdapter implements IDeviceSignedPreKeyRepository {

    private final SpringDataDeviceSignedPreKeyRepository repository;
    private final DeviceSignedPreKeyMapper mapper;

    public JpaDeviceSignedPreKeyRepositoryAdapter(
            SpringDataDeviceSignedPreKeyRepository repository,
            DeviceSignedPreKeyMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(DeviceSignedPreKey signedPreKey) {
        repository.save(mapper.toJpa(signedPreKey));
    }

    @Override
    public List<DeviceSignedPreKey> findByDeviceId(DeviceId deviceId) {
        return repository.findByIdDeviceId(deviceId.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<DeviceSignedPreKey> findActiveByDeviceId(
            DeviceId deviceId,
            Instant now
    ) {
        return repository
                .findFirstByIdDeviceIdAndExpiresAtAfterOrderByCreatedAtDesc(
                        deviceId.value(),
                        now
                )
                .map(mapper::toDomain);
    }

    @Override
    public Optional<DeviceSignedPreKey> findByDeviceIdAndKeyId(
            DeviceId deviceId,
            int keyId
    ) {
        return repository
                .findByIdDeviceIdAndIdKeyId(
                        deviceId.value(),
                        keyId
                )
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByDeviceId(DeviceId deviceId) {
        repository.deleteByIdDeviceId(deviceId.value());
    }
}