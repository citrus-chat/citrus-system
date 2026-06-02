package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.identity.domain.model.DeviceIdentity;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicIdentityKey;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceIdentityJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.springframework.stereotype.Component;

@Component
public final class DeviceIdentityMapper {

	public DeviceIdentity toDomain(DeviceIdentityJpaEntity entity) {
		if (entity == null)
			return null;

		return DeviceIdentity.reconstitute(new DeviceId(entity.getDeviceId()),
				new PublicIdentityKey(entity.getPublicIdentityKey()), entity.getCreatedAt());
	}

	public DeviceIdentityJpaEntity toJpa(DeviceIdentity domain) {
		if (domain == null)
			return null;

		return new DeviceIdentityJpaEntity(domain.getDeviceId().value(), domain.getPublicIdentityKey().value(),
				domain.getCreatedAt());
	}
}
