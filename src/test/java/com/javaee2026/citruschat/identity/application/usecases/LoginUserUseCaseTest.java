// package com.javaee2026.citruschat.identity.application.usecases;
//
// import com.javaee2026.citruschat.identity.application.commands.LoginCommand;
// import
// com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
// import
// com.javaee2026.citruschat.identity.application.commands.UploadPreKeysCommand;
// import
// com.javaee2026.citruschat.identity.application.exceptions.InvalidCredentialsException;
// import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
// import com.javaee2026.citruschat.identity.application.results.LoginResult;
// import
// com.javaee2026.citruschat.identity.application.results.RegisterOrRefreshUserDeviceResult;
// import
// com.javaee2026.citruschat.identity.application.results.UploadPreKeysResult;
// import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
// import com.javaee2026.citruschat.identity.domain.model.User;
// import com.javaee2026.citruschat.identity.domain.valueobjects.PhoneNumber;
// import com.javaee2026.citruschat.identity.domain.valueobjects.UserEmail;
// import com.javaee2026.citruschat.identity.domain.valueobjects.Username;
// import
// com.javaee2026.citruschat.identity.infrastructure.security.jwt.JwtService;
// import
// com.javaee2026.citruschat.identity.infrastructure.web.dto.request.OneTimePreKeyRequest;
// import
// com.javaee2026.citruschat.identity.infrastructure.web.dto.request.SignedPreKeyRequest;
// import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InOrder;
// import org.springframework.security.crypto.password.PasswordEncoder;
//
// import java.time.Instant;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// class LoginUserUseCaseTest {
//
// private UploadPreKeysUseCase uploadPreKeysUseCase;
// private IUserRepository userRepository;
// private PasswordEncoder passwordEncoder;
// private JwtService jwtService;
// private RegisterOrRefreshUserDeviceUseCase
// registerOrRefreshUserDeviceUseCase;
// private LoginUserUseCase loginUserUseCase;
//
// private static final UUID DEVICE_ID =
// UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
//
// @BeforeEach
// void setUp() {
// userRepository = mock(IUserRepository.class);
// passwordEncoder = mock(PasswordEncoder.class);
// jwtService = mock(JwtService.class);
// registerOrRefreshUserDeviceUseCase =
// mock(RegisterOrRefreshUserDeviceUseCase.class);
// uploadPreKeysUseCase = mock(UploadPreKeysUseCase.class);
//
// loginUserUseCase = new LoginUserUseCase(uploadPreKeysUseCase, userRepository,
// passwordEncoder, jwtService,
// registerOrRefreshUserDeviceUseCase);
// }
//
// private LoginCommand createLoginCommand(String email, String password) {
// return new LoginCommand(email, password, null, "Chrome on Windows",
// DeviceType.WEB, "identity-key",
// new SignedPreKeyRequest(1, "signed-public-key", "signature"),
// List.of(new OneTimePreKeyRequest(1, "prekey-1"), new OneTimePreKeyRequest(2,
// "prekey-2")));
// }
//
// private User createUser() {
// return new User(new
// UserId(UUID.fromString("91ae5825-9096-4c74-9447-1bf03004c36b")),
// new UserEmail("test@gmail.com"), new Username("test_test"), new
// PhoneNumber("099123456"),
// "$2a$10$hashedPassword", Instant.now(), Instant.now(), Instant.now(), null);
// }
//
// @Test
// void shouldLoginSuccessfullyWhenCredentialsAreValid() {
//
// LoginCommand command = createLoginCommand("test@gmail.com", "123456");
//
// User user = createUser();
//
// when(userRepository.findByEmail(new
// UserEmail("test@gmail.com"))).thenReturn(Optional.of(user));
//
// when(passwordEncoder.matches("123456",
// user.getPasswordHash())).thenReturn(true);
//
// when(registerOrRefreshUserDeviceUseCase.execute(any(RegisterOrRefreshUserDeviceCommand.class)))
// .thenReturn(new RegisterOrRefreshUserDeviceResult(DEVICE_ID));
//
// when(uploadPreKeysUseCase.execute(any(UploadPreKeysCommand.class))).thenReturn(new
// UploadPreKeysResult(2));
//
// when(jwtService.generateToken(user.getId().value().toString(),
// DEVICE_ID.toString(), user.getEmail().getValue(),
// user.getUsername().getValue())).thenReturn("jwt-token");
//
// when(jwtService.getExpirationInSeconds()).thenReturn(86400L);
//
// LoginResult result = loginUserUseCase.execute(command);
//
// assertNotNull(result);
// assertEquals(user.getId().value(), result.userId());
// assertEquals(user.getEmail().getValue(), result.email());
// assertEquals(user.getUsername().getValue(), result.username());
// assertEquals("jwt-token", result.accessToken());
// assertEquals("Bearer", result.tokenType());
// assertEquals(86400L, result.expiresIn());
// assertEquals(DEVICE_ID, result.deviceId());
// assertEquals(2, result.availableOneTimePreKeys());
//
// verify(userRepository).findByEmail(new UserEmail("test@gmail.com"));
//
// verify(passwordEncoder).matches("123456", user.getPasswordHash());
//
// verify(registerOrRefreshUserDeviceUseCase).execute(argThat(
// deviceCommand -> deviceCommand.deviceId() == null &&
// deviceCommand.userId().equals(user.getId().value())
// && deviceCommand.deviceName().equals("Chrome on Windows")
// && deviceCommand.deviceType() == DeviceType.WEB));
//
// verify(uploadPreKeysUseCase).execute(argThat(keysCommand ->
// keysCommand.deviceId().equals(DEVICE_ID)
// && keysCommand.publicIdentityKey().equals("identity-key") &&
// keysCommand.signedPreKey().keyId() == 1
// && keysCommand.oneTimePreKeys().size() == 2));
//
// verify(jwtService).generateToken(user.getId().value().toString(),
// DEVICE_ID.toString(),
// user.getEmail().getValue(), user.getUsername().getValue());
// }
//
// @Test
// void shouldThrowInvalidCredentialsWhenUserDoesNotExist() {
//
// LoginCommand command = createLoginCommand("missing@gmail.com", "123456");
//
// when(userRepository.findByEmail(new
// UserEmail("missing@gmail.com"))).thenReturn(Optional.empty());
//
// assertThrows(InvalidCredentialsException.class, () ->
// loginUserUseCase.execute(command));
//
// verify(userRepository).findByEmail(new UserEmail("missing@gmail.com"));
//
// verifyNoInteractions(passwordEncoder);
// verifyNoInteractions(jwtService);
// verifyNoInteractions(registerOrRefreshUserDeviceUseCase);
// verifyNoInteractions(uploadPreKeysUseCase);
// }
//
// @Test
// void shouldThrowInvalidCredentialsWhenPasswordIsInvalid() {
//
// LoginCommand command = createLoginCommand("test@gmail.com",
// "wrong-password");
//
// User user = createUser();
//
// when(userRepository.findByEmail(new
// UserEmail("test@gmail.com"))).thenReturn(Optional.of(user));
//
// when(passwordEncoder.matches("wrong-password",
// user.getPasswordHash())).thenReturn(false);
//
// assertThrows(InvalidCredentialsException.class, () ->
// loginUserUseCase.execute(command));
//
// verify(userRepository).findByEmail(new UserEmail("test@gmail.com"));
//
// verify(passwordEncoder).matches("wrong-password", user.getPasswordHash());
//
// verifyNoInteractions(jwtService);
// verifyNoInteractions(registerOrRefreshUserDeviceUseCase);
// verifyNoInteractions(uploadPreKeysUseCase);
// }
//
// @Test
// void shouldRegisterDeviceBeforeUploadingPreKeys() {
//
// LoginCommand command = createLoginCommand("test@gmail.com", "123456");
//
// User user = createUser();
//
// when(userRepository.findByEmail(new
// UserEmail("test@gmail.com"))).thenReturn(Optional.of(user));
//
// when(passwordEncoder.matches("123456",
// user.getPasswordHash())).thenReturn(true);
//
// when(registerOrRefreshUserDeviceUseCase.execute(any(RegisterOrRefreshUserDeviceCommand.class)))
// .thenReturn(new RegisterOrRefreshUserDeviceResult(DEVICE_ID));
//
// when(uploadPreKeysUseCase.execute(any(UploadPreKeysCommand.class))).thenReturn(new
// UploadPreKeysResult(2));
//
// when(jwtService.generateToken(anyString(), anyString(), anyString(),
// anyString())).thenReturn("jwt-token");
//
// when(jwtService.getExpirationInSeconds()).thenReturn(86400L);
//
// loginUserUseCase.execute(command);
//
// InOrder inOrder = inOrder(registerOrRefreshUserDeviceUseCase,
// uploadPreKeysUseCase, jwtService);
//
// inOrder.verify(registerOrRefreshUserDeviceUseCase).execute(any(RegisterOrRefreshUserDeviceCommand.class));
//
// inOrder.verify(uploadPreKeysUseCase).execute(any(UploadPreKeysCommand.class));
//
// inOrder.verify(jwtService).generateToken(anyString(), anyString(),
// anyString(), anyString());
// }
// }
