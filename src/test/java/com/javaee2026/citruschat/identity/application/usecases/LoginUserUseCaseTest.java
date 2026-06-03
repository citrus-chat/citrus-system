package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.LoginCommand;
import com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
import com.javaee2026.citruschat.identity.application.exceptions.InvalidCredentialsException;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.LoginResult;
import com.javaee2026.citruschat.identity.application.results.RegisterOrRefreshUserDeviceResult;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.valueobjects.PhoneNumber;
import com.javaee2026.citruschat.identity.domain.valueobjects.UserEmail;
import com.javaee2026.citruschat.identity.domain.valueobjects.Username;
import com.javaee2026.citruschat.identity.infrastructure.security.jwt.JwtService;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUserUseCaseTest {

	private IUserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtService jwtService;
	private RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase;
	private LoginUserUseCase loginUserUseCase;

	private static final UUID DEVICE_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

	@BeforeEach
	void setUp() {
		userRepository = mock(IUserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		jwtService = mock(JwtService.class);
		registerOrRefreshUserDeviceUseCase = mock(RegisterOrRefreshUserDeviceUseCase.class);

		loginUserUseCase = new LoginUserUseCase(userRepository, passwordEncoder, jwtService,
				registerOrRefreshUserDeviceUseCase);
	}

	@Test
	void shouldLoginSuccessfullyWhenCredentialsAreValid() {

		LoginCommand command = new LoginCommand("test@gmail.com", "123456", null, "Chrome on Windows", DeviceType.WEB);

		User user = createUser();

		when(userRepository.findByEmail(new UserEmail("test@gmail.com"))).thenReturn(Optional.of(user));

		when(passwordEncoder.matches("123456", user.getPasswordHash())).thenReturn(true);

		when(registerOrRefreshUserDeviceUseCase.execute(any(RegisterOrRefreshUserDeviceCommand.class)))
				.thenReturn(new RegisterOrRefreshUserDeviceResult(DEVICE_ID));

		when(jwtService.generateToken(user.getId().value().toString(), DEVICE_ID.toString(), user.getEmail().getValue(),
				user.getUsername().getValue())).thenReturn("jwt-token");

		when(jwtService.getExpirationInSeconds()).thenReturn(86400L);

		LoginResult result = loginUserUseCase.execute(command);

		assertNotNull(result);
		assertEquals(user.getId().value(), result.userId());
		assertEquals(user.getEmail().getValue(), result.email());
		assertEquals(user.getUsername().getValue(), result.username());
		assertEquals("jwt-token", result.accessToken());
		assertEquals("Bearer", result.tokenType());
		assertEquals(86400L, result.expiresIn());
		assertEquals(DEVICE_ID, result.deviceId());

		verify(userRepository).findByEmail(new UserEmail("test@gmail.com"));

		verify(passwordEncoder).matches("123456", user.getPasswordHash());

		verify(registerOrRefreshUserDeviceUseCase).execute(argThat(
				deviceCommand -> deviceCommand.deviceId() == null && deviceCommand.userId().equals(user.getId().value())
						&& deviceCommand.deviceName().equals("Chrome on Windows")
						&& deviceCommand.deviceType() == DeviceType.WEB));

		verify(jwtService).generateToken(user.getId().value().toString(), DEVICE_ID.toString(),
				user.getEmail().getValue(), user.getUsername().getValue());
	}

	@Test
	void shouldThrowInvalidCredentialsWhenUserDoesNotExist() {

		LoginCommand command = new LoginCommand("missing@gmail.com", "123456", null, "Chrome on Windows",
				DeviceType.WEB);

		when(userRepository.findByEmail(new UserEmail("missing@gmail.com"))).thenReturn(Optional.empty());

		assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.execute(command));

		verify(userRepository).findByEmail(new UserEmail("missing@gmail.com"));

		verifyNoInteractions(passwordEncoder);
		verifyNoInteractions(jwtService);
		verifyNoInteractions(registerOrRefreshUserDeviceUseCase);
	}

	@Test
	void shouldThrowInvalidCredentialsWhenPasswordIsInvalid() {

		LoginCommand command = new LoginCommand("test@gmail.com", "wrong-password", null, "Chrome on Windows",
				DeviceType.WEB);

		User user = createUser();

		when(userRepository.findByEmail(new UserEmail("test@gmail.com"))).thenReturn(Optional.of(user));

		when(passwordEncoder.matches("wrong-password", user.getPasswordHash())).thenReturn(false);

		assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.execute(command));

		verify(userRepository).findByEmail(new UserEmail("test@gmail.com"));

		verify(passwordEncoder).matches("wrong-password", user.getPasswordHash());

		verifyNoInteractions(jwtService);
		verifyNoInteractions(registerOrRefreshUserDeviceUseCase);
	}

	private User createUser() {
		return new User(new UserId(UUID.fromString("91ae5825-9096-4c74-9447-1bf03004c36b")),
				new UserEmail("test@gmail.com"), new Username("test_test"), new PhoneNumber("099123456"),
				"$2a$10$hashedPassword", Instant.now(), Instant.now(), Instant.now(), null);
	}
}
