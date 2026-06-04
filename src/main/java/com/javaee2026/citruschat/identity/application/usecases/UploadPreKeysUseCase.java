package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.UploadPreKeysCommand;
import com.javaee2026.citruschat.identity.application.ports.IDeviceIdentityRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceOneTimePreKeyRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceSignedPreKeyRepository;
import com.javaee2026.citruschat.identity.application.results.UploadPreKeysResult;
import com.javaee2026.citruschat.identity.domain.model.DeviceIdentity;
import com.javaee2026.citruschat.identity.domain.model.DeviceOneTimePreKey;
import com.javaee2026.citruschat.identity.domain.model.DeviceSignedPreKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.OneTimePreKeyPublicKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicIdentityKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeyPublicKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeySignature;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class UploadPreKeysUseCase {

	private final IDeviceIdentityRepository identityRepository;
	private final IDeviceSignedPreKeyRepository signedPreKeyRepository;
	private final IDeviceOneTimePreKeyRepository oneTimePreKeyRepository;

	@Transactional
	public UploadPreKeysResult execute(UploadPreKeysCommand command) {

		if (command.deviceId() == null) {
			throw new IllegalArgumentException("Device ID cannot be null");
		}

		validateUniquePreKeyIds(command);

		DeviceId deviceId = new DeviceId(command.deviceId());

		if (identityRepository.existsByDeviceId(deviceId)) {
			throw new IllegalStateException("Identity key already registered");
		}

		Instant now = Instant.now();
		PublicIdentityKey publicIdentityKey = new PublicIdentityKey(command.publicIdentityKey());

		DeviceIdentity identity = DeviceIdentity.createNew(deviceId, publicIdentityKey, now);

		identityRepository.save(identity);

		SignedPreKeyPublicKey signedPreKeyPublicKey = new SignedPreKeyPublicKey(command.signedPreKey().publicKey());
		SignedPreKeySignature signedPreKeySignature = new SignedPreKeySignature(command.signedPreKey().signature());

		DeviceSignedPreKey signedPreKey = DeviceSignedPreKey.createNew(deviceId, command.signedPreKey().keyId(),
				signedPreKeyPublicKey, signedPreKeySignature, now, null);

		signedPreKeyRepository.save(signedPreKey);

		for (var preKey : command.oneTimePreKeys()) {

			OneTimePreKeyPublicKey oneTimePreKeyPublicKey = new OneTimePreKeyPublicKey(preKey.publicKey());

			DeviceOneTimePreKey oneTimePreKey = DeviceOneTimePreKey.createNew(deviceId, preKey.keyId(),
					oneTimePreKeyPublicKey, now);

			oneTimePreKeyRepository.save(oneTimePreKey);
		}

		return new UploadPreKeysResult(command.oneTimePreKeys().size());
	}

	private void validateUniquePreKeyIds(UploadPreKeysCommand command) {
		Set<Integer> ids = new HashSet<>();

		for (var preKey : command.oneTimePreKeys()) {
			if (!ids.add(preKey.keyId())) {
				throw new IllegalArgumentException("Duplicate one-time prekey id: " + preKey.keyId());
			}
		}

		if (ids.isEmpty()) {
			throw new IllegalArgumentException("You must provide at least one one-time prekey");
		}
	}
}
