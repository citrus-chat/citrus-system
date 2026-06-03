package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IDeviceIdentityRepository;
import com.javaee2026.citruschat.identity.domain.model.DeviceIdentity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper.DeviceIdentityMapper;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaDeviceIdentityRepositoryAdapter implements IDeviceIdentityRepository {

	private final SpringDataDeviceIdentityRepository repository;
	private final DeviceIdentityMapper deviceMapper;

	public JpaDeviceIdentityRepositoryAdapter(SpringDataDeviceIdentityRepository repository,
			DeviceIdentityMapper deviceMapper) {
		this.repository = repository;
		this.deviceMapper = deviceMapper;
	}

	@Override
	public void save(DeviceIdentity deviceIdentity) {
		repository.save(deviceMapper.toJpa(deviceIdentity));
	}

	@Override
	public Optional<DeviceIdentity> findByDeviceId(DeviceId deviceId) {
		return repository.findById(deviceId.value()).map(deviceMapper::toDomain);
	}

	@Override
	public boolean existsByDeviceId(DeviceId deviceId) {
		return repository.existsById(deviceId.value());
	}
}
