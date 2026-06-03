package com.javaee2026.citruschat.identity.infrastructure.configuration;

import com.javaee2026.citruschat.identity.application.ports.*;
import com.javaee2026.citruschat.identity.application.usecases.*;
import com.javaee2026.citruschat.identity.domain.factory.UserFactory;
import com.javaee2026.citruschat.identity.domain.factory.UsernameFactory;
import com.javaee2026.citruschat.identity.infrastructure.persistence.jpa.mapper.UserMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for the Identity module.
 *
 * <p>
 * This class acts as the <b>composition root</b> for the identity context,
 * responsible for wiring together domain factories, mappers, and application
 * use cases.
 * </p>
 *
 * <p>
 * It defines how objects are instantiated and how dependencies are injected,
 * keeping the domain and application layers free from framework-specific
 * annotations.
 * </p>
 */
@Configuration
public class IdentityBeansConfiguration {

	/**
	 * Creates the {@link UserFactory} bean.
	 *
	 * @return a new instance of {@link UserFactory}
	 */
	@Bean
	public UserFactory userFactory() {
		return new UserFactory();
	}

	/**
	 * Creates the {@link UsernameFactory} bean.
	 *
	 * @return a new instance of {@link UsernameFactory}
	 */
	@Bean
	public UsernameFactory usernameFactory() {
		return new UsernameFactory();
	}

	/**
	 * Creates the {@link UserMapper} bean.
	 *
	 * @param userFactory
	 *            factory used to reconstitute domain objects
	 * @return a configured {@link UserMapper}
	 */
	@Bean
	public UserMapper userMapper(final UserFactory userFactory) {
		return new UserMapper(userFactory);
	}

	/**
	 * Creates the {@link RegisterUserUseCase} bean.
	 *
	 * <p>
	 * This use case is responsible for registering new users, including: generating
	 * a temporary password, hashing it, and creating the user entity.
	 * </p>
	 *
	 * @param userRepository
	 *            repository used to persist users
	 * @param defaultPasswordGenerator
	 *            generator for temporary passwords
	 * @param passwordHasher
	 *            component used to hash passwords
	 * @param userFactory
	 *            factory to create user domain objects
	 * @param usernameFactory
	 *            factory to generate usernames
	 * @return a configured {@link RegisterUserUseCase}
	 */
	@Bean
	public RegisterUserUseCase registerUserUseCase(final IUserRepository userRepository,
			final IDefaultPasswordGenerator defaultPasswordGenerator, final IPasswordHasher passwordHasher,
			final UserFactory userFactory, final UsernameFactory usernameFactory) {
		return new RegisterUserUseCase(userRepository, defaultPasswordGenerator, passwordHasher, userFactory,
				usernameFactory);
	}

	/**
	 * Creates the {@link ValidateUserAccountUseCase} bean.
	 *
	 * <p>
	 * This use case handles the initial account activation flow, validating the
	 * temporary password and setting a new password.
	 * </p>
	 *
	 * @param userRepository
	 *            repository used to retrieve and persist users
	 * @param passwordHasher
	 *            component used to verify and hash passwords
	 * @return a configured {@link ValidateUserAccountUseCase}
	 */
	@Bean
	public ValidateUserAccountUseCase validateUserAccountUseCase(final IUserRepository userRepository,
			final IPasswordHasher passwordHasher) {
		return new ValidateUserAccountUseCase(userRepository, passwordHasher);
	}

	@Bean
	public CheckAdminAccessUseCase checkAdminAccessUseCase(IAdminAccessRepository adminAccessRepository) {
		return new CheckAdminAccessUseCase(adminAccessRepository);
	}

	@Bean
	public RegisterOrRefreshUserDeviceUseCase registerOrRefreshUserDeviceUseCase(
			IUserDeviceRepository userDeviceRepository) {
		return new RegisterOrRefreshUserDeviceUseCase(userDeviceRepository);
	}

	@Bean
	public GetCurrentUserDevicesUseCase getCurrentUserDevicesUseCase(IUserDeviceRepository userDeviceRepository) {
		return new GetCurrentUserDevicesUseCase(userDeviceRepository);
	}
	@Bean
	public ValidateUserDeviceOwnershipUseCase validateUserDeviceOwnershipUseCase(
			IUserDeviceRepository userDeviceRepository) {
		return new ValidateUserDeviceOwnershipUseCase(userDeviceRepository);
	}
}
