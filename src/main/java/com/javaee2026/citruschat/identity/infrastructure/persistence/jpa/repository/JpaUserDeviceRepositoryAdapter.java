package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.repository;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper.UserDeviceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserDeviceRepositoryAdapter implements IUserDeviceRepository {

	private final SpringDataUserDeviceRepository repository;

	public JpaUserDeviceRepositoryAdapter(SpringDataUserDeviceRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<UserDevice> findActiveByIdAndUserId(UUID deviceId, UUID userId) {
		return repository.findByIdAndUserIdAndRevokedAtIsNull(deviceId, userId).map(UserDeviceMapper::toDomain);
	}

	// @Override
	// public Optional<UserDevice> findActiveByUserIdAndDeviceType(UUID userId,
	// DeviceType deviceType) {
	// return repository.findByUserIdAndDeviceTypeAndRevokedAtIsNull(userId,
	// deviceType)
	// .map(UserDeviceMapper::toDomain);
	// }

	@Override
	public List<UserDevice> findActiveByUserId(UUID userId) {
		return repository.findAllByUserIdAndRevokedAtIsNull(userId).stream().map(UserDeviceMapper::toDomain).toList();
	}

	@Override
	public Optional<UserDevice> findActiveById(UUID deviceId) {
		return repository.findByIdAndRevokedAtIsNull(deviceId).map(UserDeviceMapper::toDomain);
	}

	@Override
	public boolean existsByUserIdAndRevokedAtIsNull(UUID userId) {
		return repository.existsByUserIdAndRevokedAtIsNull(userId);
	}

	@Override
	public boolean existsActiveByIdAndUserId(UUID deviceId, UUID userId) {
		return repository.existsByIdAndUserIdAndRevokedAtIsNull(deviceId, userId);
	}

	@Override
	public List<UserDevice> findAllByUserId(UUID userId) {
		return repository.findUserDeviceJpaEntitiesByUserId(userId).stream().map(UserDeviceMapper::toDomain).toList();
	}

	@Override
	public UserDevice save(UserDevice userDevice) {
		var saved = repository.save(UserDeviceMapper.toEntity(userDevice));
		return UserDeviceMapper.toDomain(saved);
	}
}
