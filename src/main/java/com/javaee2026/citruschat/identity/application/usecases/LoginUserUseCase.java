package com.javaee2026.citruschat.identity.application.usecases;

import com.javaee2026.citruschat.identity.application.commands.LoginCommand;
import com.javaee2026.citruschat.identity.application.commands.RegisterOrRefreshUserDeviceCommand;
import com.javaee2026.citruschat.identity.application.exceptions.InvalidCredentialsException;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.application.results.LoginResult;
import com.javaee2026.citruschat.identity.application.results.RegisterOrRefreshUserDeviceResult;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.valueobjects.UserEmail;
import com.javaee2026.citruschat.identity.infrastructure.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUserUseCase {

	private final RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase;
	private final IUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public LoginUserUseCase(IUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.registerOrRefreshUserDeviceUseCase = registerOrRefreshUserDeviceUseCase;
	}

	public LoginResult execute(LoginCommand command) {

		UserEmail email = new UserEmail(command.email());
		User user = userRepository.findByEmail(email).orElseThrow(InvalidCredentialsException::new);

		boolean validPassword = passwordEncoder.matches(command.password(), user.getPasswordHash());

		if (!validPassword) {
			throw new InvalidCredentialsException();
		}
		RegisterOrRefreshUserDeviceResult deviceResult = registerOrRefreshUserDeviceUseCase.execute(
				new RegisterOrRefreshUserDeviceCommand(command.deviceId(), user.getId().value(), command.deviceName(),
						command.deviceType(), command.publicIdentityKey(), command.signedPrekey()));

		String accessToken = jwtService.generateToken(user.getId().value().toString(), user.getEmail().getValue(),
				user.getUsername().getValue());
		long expiresIn = jwtService.getExpirationInSeconds();

		return new LoginResult(user.getId().value(), user.getEmail().getValue(), user.getUsername().getValue(),
				accessToken, "Bearer", expiresIn, deviceResult.deviceId());
	}
}
