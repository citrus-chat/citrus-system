package com.javaee2026.citruschat.identity.infrastructure.web.mapper;

import com.javaee2026.citruschat.identity.application.results.UserDeviceKeysResult;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.DeviceResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserDeviceKeysResponse;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.response.UserResponse;

import java.util.List;

public final class UserWebMapper {

	private UserWebMapper() {
	}

	public static UserResponse toResponse(UserResult result) {
		return new UserResponse(result.getId().toString(), result.getUsername(), result.getEmail(), result.isActive(),
				result.getAvatarUrl());
	}

	public static List<UserResponse> toResponseList(List<UserResult> results) {
		return results.stream().map(UserWebMapper::toResponse).toList();
	}

	public static UserDeviceKeysResponse toUserDeviceKeysResponse(UserDeviceKeysResult result) {

		return new UserDeviceKeysResponse(result.userId().value(), result.devices().stream()
				.map(device -> new DeviceResponse(device.deviceId().value(), device.publicKey())).toList());
	}
}
