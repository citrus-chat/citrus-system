package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.DeviceKeyResult;
import com.javaee2026.citruschat.identity.application.results.UserDeviceKeysResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserDeviceKeysUseCase {

	private final IUserRepository userRepository;
	private final IUserDeviceRepository deviceRepository;

	public UserDeviceKeysResult execute(UUID userId) {
		User user = userRepository.findById(new UserId(userId))
				.orElseThrow(() -> new RuntimeException("User not found"));

		List<UserDevice> devices = deviceRepository.findActiveByUserId(user.getId().value());

		if (devices.isEmpty()) {
			throw new RuntimeException("The user has no devices.");
		}

		List<DeviceKeyResult> results = new ArrayList<>();

		for (UserDevice device : devices) {
			results.add(new DeviceKeyResult(device.getId(), device.getPublicKey().value()));
		}

		return new UserDeviceKeysResult(user.getId(), results);
	}

}
