package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.UploadPreKeysCommand;
import com.javaee2026.citruschat.identity.application.ports.IDeviceIdentityRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceOneTimePreKeyRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceSignedPreKeyRepository;
import com.javaee2026.citruschat.identity.application.results.UploadPreKeysResult;
import com.javaee2026.citruschat.identity.domain.model.DeviceIdentity;
import com.javaee2026.citruschat.identity.domain.model.DeviceOneTimePreKey;
import com.javaee2026.citruschat.identity.domain.model.DeviceSignedPreKey;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class UploadPreKeysUseCaseTest {

	private IDeviceIdentityRepository identityRepository;
	private IDeviceSignedPreKeyRepository signedPreKeyRepository;
	private IDeviceOneTimePreKeyRepository oneTimePreKeyRepository;

	private UploadPreKeysUseCase uploadPreKeysUseCase;

	private static final UUID DEVICE_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	private static final String IDENTITY_KEY = "QUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUE=";
	private static final String SIGNED_PREKEY = "QkJCQkJCQkJCQkJCQkJCQkJCQkJCQkJCQkJCQkJCQkI=";
	private static final String SIGNATURE = "Q0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0M=";
	private static final String PREKEY_1 = "REREREREREREREREREREREREREREREREREREREREREQ=";
	private static final String PREKEY_2 = "RUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUU=";

	@BeforeEach
	void setUp() {
		identityRepository = mock(IDeviceIdentityRepository.class);
		signedPreKeyRepository = mock(IDeviceSignedPreKeyRepository.class);
		oneTimePreKeyRepository = mock(IDeviceOneTimePreKeyRepository.class);

		uploadPreKeysUseCase = new UploadPreKeysUseCase(identityRepository, signedPreKeyRepository,
				oneTimePreKeyRepository);
	}

	@Test
	void shouldUploadPreKeysSuccessfully() {

		UploadPreKeysCommand command = createCommand();

		when(identityRepository.existsByDeviceId(any(DeviceId.class))).thenReturn(false);

		UploadPreKeysResult result = uploadPreKeysUseCase.execute(command);

		assertNotNull(result);
		assertEquals("Expected 2 available keys", 2, result.availableKeys());

		verify(identityRepository).existsByDeviceId(new DeviceId(DEVICE_ID));

		verify(identityRepository).save(any(DeviceIdentity.class));

		verify(signedPreKeyRepository).save(any(DeviceSignedPreKey.class));

		verify(oneTimePreKeyRepository, times(2)).save(any(DeviceOneTimePreKey.class));
	}

	@Test
	void shouldThrowExceptionWhenDeviceIdIsNull() {

		UploadPreKeysCommand command = new UploadPreKeysCommand(null, IDENTITY_KEY,
				new UploadPreKeysCommand.SignedPreKeyCommand(1, SIGNED_PREKEY, SIGNATURE),
				List.of(new UploadPreKeysCommand.OneTimePreKeyCommand(1, PREKEY_1)));

		assertThrows(IllegalArgumentException.class, () -> uploadPreKeysUseCase.execute(command));

		verifyNoInteractions(identityRepository);
		verifyNoInteractions(signedPreKeyRepository);
		verifyNoInteractions(oneTimePreKeyRepository);
	}

	@Test
	void shouldThrowExceptionWhenIdentityAlreadyExists() {

		UploadPreKeysCommand command = createCommand();

		when(identityRepository.existsByDeviceId(any(DeviceId.class))).thenReturn(true);

		assertThrows(IllegalStateException.class, () -> uploadPreKeysUseCase.execute(command));

		verify(identityRepository).existsByDeviceId(new DeviceId(DEVICE_ID));

		verify(identityRepository, never()).save(any());
		verifyNoInteractions(signedPreKeyRepository);
		verifyNoInteractions(oneTimePreKeyRepository);
	}

	@Test
	void shouldThrowExceptionWhenNoOneTimePreKeysAreProvided() {

		UploadPreKeysCommand command = new UploadPreKeysCommand(DEVICE_ID, IDENTITY_KEY,
				new UploadPreKeysCommand.SignedPreKeyCommand(1, SIGNED_PREKEY, SIGNATURE), List.of());

		assertThrows(IllegalArgumentException.class, () -> uploadPreKeysUseCase.execute(command));

		verifyNoInteractions(identityRepository);
		verifyNoInteractions(signedPreKeyRepository);
		verifyNoInteractions(oneTimePreKeyRepository);
	}

	@Test
	void shouldThrowExceptionWhenDuplicateOneTimePreKeyIdsAreProvided() {

		UploadPreKeysCommand command = new UploadPreKeysCommand(DEVICE_ID, IDENTITY_KEY,
				new UploadPreKeysCommand.SignedPreKeyCommand(1, SIGNED_PREKEY, SIGNATURE),
				List.of(new UploadPreKeysCommand.OneTimePreKeyCommand(10, PREKEY_1),
						new UploadPreKeysCommand.OneTimePreKeyCommand(10, PREKEY_2)));

		assertThrows(IllegalArgumentException.class, () -> uploadPreKeysUseCase.execute(command));

		verifyNoInteractions(identityRepository);
		verifyNoInteractions(signedPreKeyRepository);
		verifyNoInteractions(oneTimePreKeyRepository);
	}

	private UploadPreKeysCommand createCommand() {
		return new UploadPreKeysCommand(DEVICE_ID, IDENTITY_KEY,
				new UploadPreKeysCommand.SignedPreKeyCommand(1, SIGNED_PREKEY, SIGNATURE),
				List.of(new UploadPreKeysCommand.OneTimePreKeyCommand(1, PREKEY_1),
						new UploadPreKeysCommand.OneTimePreKeyCommand(2, PREKEY_2)));
	}
}
