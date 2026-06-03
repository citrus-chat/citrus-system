package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IDeviceOneTimePreKeyRepository;
import com.javaee2026.citruschat.identity.domain.model.DeviceOneTimePreKey;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper.DeviceOneTimePreKeyMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaDeviceOneTimePreKeyRepositoryAdapter implements IDeviceOneTimePreKeyRepository {

    private final SpringDataDeviceOneTimePreKeyRepository repository;
    private final DeviceOneTimePreKeyMapper mapper;

    public JpaDeviceOneTimePreKeyRepositoryAdapter(SpringDataDeviceOneTimePreKeyRepository repository, DeviceOneTimePreKeyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(DeviceOneTimePreKey oneTimePreKey) {
        repository.save(mapper.toJpa(oneTimePreKey));
    }

    @Override
    public List<DeviceOneTimePreKey> findByDeviceId(DeviceId deviceId) {
        return repository.findByIdDeviceId(deviceId.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<DeviceOneTimePreKey> findByDeviceIdAndKeyId(
            DeviceId deviceId,
            int keyId
    ) {
        return repository
                .findByIdDeviceIdAndIdKeyId(deviceId.value(), keyId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<DeviceOneTimePreKey> findFirstAvailable(DeviceId deviceId) {
        return repository
                .findFirstByIdDeviceIdAndConsumedAtIsNull(deviceId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByDeviceId(DeviceId deviceId) {
        repository.deleteByIdDeviceId(deviceId.value());
    }
}