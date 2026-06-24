package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.results.DevicePublicKeyResult;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.DevicePublicKeyResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserDeviceResponse;

public final class UserDeviceWebMapper {

	private UserDeviceWebMapper() {
	}

	public static UserDeviceResponse toResponse(UserDevice device) {
		return new UserDeviceResponse(device.getId().value(), device.getPublicKey().value(), device.getDeviceName(),
				device.getDeviceType(), device.getLastSeen(), device.getCreatedAt());
	}

	public static DevicePublicKeyResponse toDevicePublicKeyResponse(DevicePublicKeyResult result) {

		return new DevicePublicKeyResponse(result.deviceId(), result.publicKey());
	}
}
