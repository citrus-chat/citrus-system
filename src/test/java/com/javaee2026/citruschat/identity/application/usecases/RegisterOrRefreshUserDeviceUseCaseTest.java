package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
import com.javaee2026.citruschat.identity.application.exceptions.IllegalPublicKeyException;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.results.RegisterOrRefreshUserDeviceResult;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterOrRefreshUserDeviceUseCaseTest {

	private final IUserDeviceRepository userDeviceRepository = mock(IUserDeviceRepository.class);

	private final RegisterOrRefreshUserDeviceUseCase useCase = new RegisterOrRefreshUserDeviceUseCase(
			userDeviceRepository);

	@Test
	void shouldRefreshExistingDeviceWhenPublicKeyMatchesRawValue() {
		UUID userId = UUID.randomUUID();
		UUID deviceId = UUID.randomUUID();
		String publicKey = "AQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHyA=";
		UserDevice existingDevice = createDevice(userId, deviceId, publicKey);

		when(userDeviceRepository.findActiveByIdAndUserId(deviceId, userId)).thenReturn(Optional.of(existingDevice));
		when(userDeviceRepository.save(existingDevice)).thenReturn(existingDevice);

		RegisterOrRefreshUserDeviceResult result = useCase.execute(
				new RegisterOrRefreshUserDeviceCommand(deviceId, userId, publicKey, "Pixel 9", DeviceType.MOBILE));

		assertEquals(deviceId, result.deviceId());
		verify(userDeviceRepository).save(existingDevice);
	}

	@Test
	void shouldRejectExistingDeviceWhenPublicKeyChanges() {
		UUID userId = UUID.randomUUID();
		UUID deviceId = UUID.randomUUID();
		String storedPublicKey = "AQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHyA=";
		String changedPublicKey = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";

		when(userDeviceRepository.findActiveByIdAndUserId(deviceId, userId))
				.thenReturn(Optional.of(createDevice(userId, deviceId, storedPublicKey)));

		assertThrows(IllegalPublicKeyException.class,
				() -> useCase.execute(new RegisterOrRefreshUserDeviceCommand(deviceId, userId, changedPublicKey,
						"Pixel 9", DeviceType.MOBILE)));
	}

	@Test
	void shouldCreateNewDeviceWithRawPublicKey() {
		UUID userId = UUID.randomUUID();
		UUID requestedDeviceId = UUID.randomUUID();
		UUID savedDeviceId = UUID.randomUUID();
		String publicKey = "AQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHyA=";
		UserDevice savedDevice = createDevice(userId, savedDeviceId, publicKey);

		when(userDeviceRepository.findActiveByIdAndUserId(requestedDeviceId, userId)).thenReturn(Optional.empty());
		when(userDeviceRepository.save(any(UserDevice.class))).thenReturn(savedDevice);

		RegisterOrRefreshUserDeviceResult result = useCase.execute(new RegisterOrRefreshUserDeviceCommand(
				requestedDeviceId, userId, publicKey, "Pixel 9", DeviceType.MOBILE));

		assertEquals(savedDeviceId, result.deviceId());
	}

	private UserDevice createDevice(UUID userId, UUID deviceId, String publicKey) {
		Instant now = Instant.now();
		return UserDevice.reconstitute(new DeviceId(deviceId), new UserId(userId), new PublicKey(publicKey), "device",
				DeviceType.MOBILE, now, now, now, null);
	}
}
