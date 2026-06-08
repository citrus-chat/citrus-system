package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.GetDevicePreKeysCommand;
import com.javaee2026.citruschat.identity.application.ports.IDeviceIdentityRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceOneTimePreKeyRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceSignedPreKeyRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.results.GetDevicePreKeysResult;
import com.javaee2026.citruschat.identity.domain.model.DeviceIdentity;
import com.javaee2026.citruschat.identity.domain.model.DeviceOneTimePreKey;
import com.javaee2026.citruschat.identity.domain.model.DeviceSignedPreKey;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
public class GetDevicePreKeysUseCase {

	private final IUserDeviceRepository userDeviceRepository;
	private final IDeviceIdentityRepository deviceIdentityRepository;
	private final IDeviceSignedPreKeyRepository deviceSignedPreKeyRepository;
	private final IDeviceOneTimePreKeyRepository deviceOneTimePreKeyRepository;

	@Transactional
	public GetDevicePreKeysResult execute(GetDevicePreKeysCommand command) {

		UserDevice device = userDeviceRepository.findActiveById(command.deviceId()).orElseThrow(
				() -> new IllegalArgumentException("Device with id " + command.deviceId() + " does not exist"));

		DeviceIdentity identity = deviceIdentityRepository.findByDeviceId(device.getId()).orElseThrow(
				() -> new IllegalStateException("Identity key not found for device " + device.getId().value()));

		DeviceSignedPreKey signedPreKey = deviceSignedPreKeyRepository
				.findActiveByDeviceId(device.getId(), Instant.now()).orElseThrow(() -> new IllegalStateException(
						"Active signed prekey not found for device " + device.getId().value()));

		Optional<DeviceOneTimePreKey> oneTimePreKeyOptional = deviceOneTimePreKeyRepository
				.findFirstAvailable(device.getId());

		Integer oneTimePreKeyId = null;
		String oneTimePreKeyValue = null;

		if (oneTimePreKeyOptional.isPresent()) {

			DeviceOneTimePreKey oneTimePreKey = oneTimePreKeyOptional.get();

			oneTimePreKey.markAsConsumed(Instant.now());
			deviceOneTimePreKeyRepository.save(oneTimePreKey);

			oneTimePreKeyId = oneTimePreKey.getKeyId();
			oneTimePreKeyValue = oneTimePreKey.getPublicKey().value();
		}

		return new GetDevicePreKeysResult(device.getId().value(),

				identity.getPublicIdentityKey().value(),

				signedPreKey.getKeyId(), signedPreKey.getPublicKey().value(), signedPreKey.getSignature().value(),

				oneTimePreKeyId, oneTimePreKeyValue);
	}

}
