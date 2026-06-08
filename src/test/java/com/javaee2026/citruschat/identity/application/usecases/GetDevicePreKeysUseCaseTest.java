package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.GetDevicePreKeysCommand;
import com.javaee2026.citruschat.identity.application.ports.IDeviceIdentityRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceOneTimePreKeyRepository;
import com.javaee2026.citruschat.identity.application.ports.IDeviceSignedPreKeyRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.results.GetDevicePreKeysResult;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.DeviceIdentity;
import com.javaee2026.citruschat.identity.domain.model.DeviceOneTimePreKey;
import com.javaee2026.citruschat.identity.domain.model.DeviceSignedPreKey;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.valueobjects.OneTimePreKeyPublicKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicIdentityKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeyPublicKey;
import com.javaee2026.citruschat.identity.domain.valueobjects.SignedPreKeySignature;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetDevicePreKeysUseCaseTest {

	private GetDevicePreKeysUseCase useCase;

	private IUserDeviceRepository userDeviceRepository;
	private IDeviceIdentityRepository deviceIdentityRepository;
	private IDeviceSignedPreKeyRepository deviceSignedPreKeyRepository;
	private IDeviceOneTimePreKeyRepository deviceOneTimePreKeyRepository;
	private Clock clock;

	private static final UUID DEVICE_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	private static final String IDENTITY_KEY = "QUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUE=";
	private static final String SIGNED_PREKEY = "QkJCQkJCQkJCQkJCQkJCQkJCQkJCQkJCQkJCQkJCQkI=";
	private static final String SIGNATURE = "Q0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0M=";
	private static final String PREKEY_1 = "REREREREREREREREREREREREREREREREREREREREREQ=";
	private static final String PREKEY_2 = "RUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUU=";
	private static final Instant NOW = Instant.now();

	@BeforeEach
	void setUp() {

		userDeviceRepository = mock(IUserDeviceRepository.class);
		deviceIdentityRepository = mock(IDeviceIdentityRepository.class);
		deviceSignedPreKeyRepository = mock(IDeviceSignedPreKeyRepository.class);
		deviceOneTimePreKeyRepository = mock(IDeviceOneTimePreKeyRepository.class);

		useCase = new GetDevicePreKeysUseCase(userDeviceRepository, deviceIdentityRepository,
				deviceSignedPreKeyRepository, deviceOneTimePreKeyRepository);
	}

	private UserDevice createDevice() {

		return UserDevice.reconstitute(new DeviceId(DEVICE_ID), new UserId(UUID.randomUUID()), "Chrome", DeviceType.WEB,
				NOW, NOW, null);
	}

	private DeviceIdentity createIdentity() {

		return DeviceIdentity.createNew(new DeviceId(DEVICE_ID), new PublicIdentityKey(IDENTITY_KEY), NOW);
	}

	private DeviceSignedPreKey createSignedPreKey() {

		return DeviceSignedPreKey.createNew(new DeviceId(DEVICE_ID), 5, new SignedPreKeyPublicKey(SIGNED_PREKEY),
				new SignedPreKeySignature(SIGNATURE), NOW, NOW.plusSeconds(86400));
	}

	private DeviceOneTimePreKey createOneTimePreKey() {

		return DeviceOneTimePreKey.createNew(new DeviceId(DEVICE_ID), 10, new OneTimePreKeyPublicKey(PREKEY_1), NOW);
	}

	@Test
	void shouldReturnBundleAndConsumeOneTimePreKey() {

		UserDevice device = createDevice();
		DeviceIdentity identity = createIdentity();
		DeviceSignedPreKey signedPreKey = createSignedPreKey();
		DeviceOneTimePreKey oneTimePreKey = createOneTimePreKey();

		when(userDeviceRepository.findActiveById(DEVICE_ID)).thenReturn(Optional.of(device));

		when(deviceIdentityRepository.findByDeviceId(device.getId())).thenReturn(Optional.of(identity));

		when(deviceSignedPreKeyRepository.findActiveByDeviceId(eq(device.getId()), any(Instant.class)))
				.thenReturn(Optional.of(signedPreKey));

		when(deviceOneTimePreKeyRepository.findFirstAvailable(device.getId())).thenReturn(Optional.of(oneTimePreKey));

		GetDevicePreKeysResult result = useCase.execute(new GetDevicePreKeysCommand(DEVICE_ID));

		assertNotNull(result);

		assertEquals(DEVICE_ID, result.deviceId());

		assertEquals(IDENTITY_KEY, result.identityKey());

		assertEquals(5, result.signedPreKeyId());
		assertEquals(SIGNED_PREKEY, result.signedPreKey());
		assertEquals(SIGNATURE, result.signedPreKeySignature());

		assertEquals(10, result.oneTimePreKeyId());
		assertEquals(PREKEY_1, result.oneTimePreKey());

		assertTrue(oneTimePreKey.isConsumed());

		verify(deviceOneTimePreKeyRepository).save(oneTimePreKey);
	}

	@Test
	void shouldReturnBundleWhenNoOneTimePreKeysExist() {

		UserDevice device = createDevice();
		DeviceIdentity identity = createIdentity();
		DeviceSignedPreKey signedPreKey = createSignedPreKey();

		when(userDeviceRepository.findActiveById(DEVICE_ID)).thenReturn(Optional.of(device));

		when(deviceIdentityRepository.findByDeviceId(device.getId())).thenReturn(Optional.of(identity));

		when(deviceSignedPreKeyRepository.findActiveByDeviceId(eq(device.getId()), any(Instant.class)))
				.thenReturn(Optional.of(signedPreKey));

		when(deviceOneTimePreKeyRepository.findFirstAvailable(device.getId())).thenReturn(Optional.empty());

		GetDevicePreKeysResult result = useCase.execute(new GetDevicePreKeysCommand(DEVICE_ID));

		assertNotNull(result);

		assertNull(result.oneTimePreKeyId());
		assertNull(result.oneTimePreKey());

		verify(deviceOneTimePreKeyRepository, never()).save(any());
	}

	@Test
	void shouldThrowExceptionWhenDeviceDoesNotExist() {

		when(userDeviceRepository.findActiveById(DEVICE_ID)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> useCase.execute(new GetDevicePreKeysCommand(DEVICE_ID)));

		verifyNoInteractions(deviceIdentityRepository);
		verifyNoInteractions(deviceSignedPreKeyRepository);
		verifyNoInteractions(deviceOneTimePreKeyRepository);
	}

	@Test
	void shouldThrowExceptionWhenIdentityKeyDoesNotExist() {

		UserDevice device = createDevice();

		when(userDeviceRepository.findActiveById(DEVICE_ID)).thenReturn(Optional.of(device));

		when(deviceIdentityRepository.findByDeviceId(device.getId())).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, () -> useCase.execute(new GetDevicePreKeysCommand(DEVICE_ID)));

		verifyNoInteractions(deviceSignedPreKeyRepository);
		verifyNoInteractions(deviceOneTimePreKeyRepository);
	}

	@Test
	void shouldThrowExceptionWhenSignedPreKeyDoesNotExist() {

		UserDevice device = createDevice();
		DeviceIdentity identity = createIdentity();

		when(userDeviceRepository.findActiveById(DEVICE_ID)).thenReturn(Optional.of(device));

		when(deviceIdentityRepository.findByDeviceId(device.getId())).thenReturn(Optional.of(identity));

		when(deviceSignedPreKeyRepository.findActiveByDeviceId(device.getId(), NOW)).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, () -> useCase.execute(new GetDevicePreKeysCommand(DEVICE_ID)));

		verifyNoInteractions(deviceOneTimePreKeyRepository);
	}

	@Test
	void shouldPersistConsumedOneTimePreKey() {

		UserDevice device = createDevice();
		DeviceIdentity identity = createIdentity();
		DeviceSignedPreKey signedPreKey = createSignedPreKey();
		DeviceOneTimePreKey oneTimePreKey = createOneTimePreKey();

		when(userDeviceRepository.findActiveById(DEVICE_ID)).thenReturn(Optional.of(device));

		when(deviceIdentityRepository.findByDeviceId(device.getId())).thenReturn(Optional.of(identity));

		when(deviceSignedPreKeyRepository.findActiveByDeviceId(eq(device.getId()), any(Instant.class)))
				.thenReturn(Optional.of(signedPreKey));

		when(deviceOneTimePreKeyRepository.findFirstAvailable(device.getId())).thenReturn(Optional.of(oneTimePreKey));

		useCase.execute(new GetDevicePreKeysCommand(DEVICE_ID));

		verify(deviceOneTimePreKeyRepository).save(argThat(savedPreKey -> savedPreKey.isConsumed()
				&& savedPreKey.getConsumedAt() != null && savedPreKey.getKeyId() == 10));
	}

}
