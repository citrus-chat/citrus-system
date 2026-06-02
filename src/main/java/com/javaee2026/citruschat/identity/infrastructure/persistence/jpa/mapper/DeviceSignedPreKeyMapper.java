package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.identity.domain.model.DeviceSignedPreKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeyPublicKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeySignature;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceSignedPreKeyJpaEntity;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.DeviceSignedPreKeyJpaId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.springframework.stereotype.Component;

@Component
public class DeviceSignedPreKeyMapper {

	public DeviceSignedPreKey toDomain(DeviceSignedPreKeyJpaEntity entity) {
		if (entity == null)
			return null;

		return DeviceSignedPreKey.reconstitute(new DeviceId(entity.getId().getDeviceId()), entity.getId().getKeyId(),
				new SignedPreKeyPublicKey(entity.getPublicKey()), new SignedPreKeySignature(entity.getSignature()),
				entity.getCreatedAt(), entity.getExpiresAt());
	}

	public DeviceSignedPreKeyJpaEntity toJpa(DeviceSignedPreKey deviceSignedPreKey) {
		if (deviceSignedPreKey == null)
			return null;

		return new DeviceSignedPreKeyJpaEntity(
				new DeviceSignedPreKeyJpaId(deviceSignedPreKey.getDeviceId().value(), deviceSignedPreKey.getKeyId()),
				deviceSignedPreKey.getPublicKey().value(), deviceSignedPreKey.getSignature().value(),
				deviceSignedPreKey.getCreatedAt(), deviceSignedPreKey.getExpiresAt());
	}
}
