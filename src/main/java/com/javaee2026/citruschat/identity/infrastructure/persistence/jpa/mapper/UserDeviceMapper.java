package com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper;

import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.entity.UserDeviceJpaEntity;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.springframework.stereotype.Component;

@Component
public final class UserDeviceMapper {

	private UserDeviceMapper() {
	}

	public static UserDevice toDomain(UserDeviceJpaEntity entity) {
		return UserDevice.reconstitute(new DeviceId(entity.getId()), new UserId(entity.getUserId()),
				new PublicKey(entity.getPublicKey()), entity.getDeviceName(), entity.getDeviceType(),
				entity.getLastSeen(), entity.getCreatedAt(), entity.getRevokedAt());
	}

	public static UserDeviceJpaEntity toEntity(UserDevice device) {
		return new UserDeviceJpaEntity(device.getId().value(), device.getUserId().value(),
				device.getPublicKey().value(), device.getDeviceName(), device.getDeviceType(), device.getLastSeen(),
				device.getCreatedAt(), device.getRevokedAt());
	}
}
