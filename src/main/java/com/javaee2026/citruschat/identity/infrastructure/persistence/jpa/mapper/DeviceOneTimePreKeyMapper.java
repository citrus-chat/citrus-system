package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.identity.domain.model.DeviceOneTimePreKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.OneTimePreKeyPublicKey;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceOneTimePreKeyJpaEntity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceOneTimePreKeyJpaId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.springframework.stereotype.Component;

@Component
public class DeviceOneTimePreKeyMapper {

	public DeviceOneTimePreKey toDomain(DeviceOneTimePreKeyJpaEntity entity) {
		if (entity == null)
			return null;

		return DeviceOneTimePreKey.reconstitute(new DeviceId(entity.getId().getDeviceId()), entity.getId().getKeyId(),
				new OneTimePreKeyPublicKey(entity.getPublicKey()), entity.getCreatedAt(), entity.getConsumedAt());
	}

	public DeviceOneTimePreKeyJpaEntity toJpa(DeviceOneTimePreKey deviceOneTimePreKey) {
		if (deviceOneTimePreKey == null)
			return null;

		return new DeviceOneTimePreKeyJpaEntity(
				new DeviceOneTimePreKeyJpaId(deviceOneTimePreKey.getDeviceId().value(), deviceOneTimePreKey.getKeyId()),
				deviceOneTimePreKey.getPublicKey().value(), deviceOneTimePreKey.getCreatedAt(),
				deviceOneTimePreKey.getConsumedAt());
	}
}
