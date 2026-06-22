package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.usecases.DeleteUserAvatarUseCase;
import com.javaee2026.citruschat.identity.application.usecases.UpdateUserAvatarUseCase;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserAvatarControllerTest {

	private static final UUID USER_ID = UUID.fromString("91ae5825-9096-4c74-9447-1bf03004c36b");
	private static final byte[] PNG_BYTES = new byte[]{(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UpdateUserAvatarUseCase updateUserAvatarUseCase;

	@MockitoBean
	private DeleteUserAvatarUseCase deleteUserAvatarUseCase;

	@AfterEach
	void cleanUp() throws Exception {
		Files.deleteIfExists(Path.of("uploads/avatars/test-avatar.png"));
	}

	@Test
	void shouldUpdateCurrentUserAvatar() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "image/png", PNG_BYTES);
		String avatarUrl = "/api/v1/users/avatars/avatar.png";

		when(updateUserAvatarUseCase.execute(any()))
				.thenReturn(new UserResult(USER_ID, "test_user", "test@gmail.com", true, avatarUrl));

		mockMvc.perform(multipart(ApiRoutes.API_USER_ME_AVATAR).file(file).with(request -> {
			request.setMethod("PUT");
			return request;
		}).with(jwt().jwt(jwtToken()))).andExpect(status().isOk()).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.USER_AVATAR_UPDATED_SUCCESS)))
				.andExpect(jsonPath("$.data.avatar_url", is(avatarUrl)));
	}

	@Test
	void shouldDeleteCurrentUserAvatar() throws Exception {
		when(deleteUserAvatarUseCase.execute(USER_ID))
				.thenReturn(new UserResult(USER_ID, "test_user", "test@gmail.com", true, null));

		mockMvc.perform(delete(ApiRoutes.API_USER_ME_AVATAR).with(jwt().jwt(jwtToken()))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.USER_AVATAR_DELETED_SUCCESS)))
				.andExpect(jsonPath("$.data.avatar_url").doesNotExist());
	}

	@Test
	void shouldExposeAvatarImage() throws Exception {
		Path avatar = Path.of("uploads/avatars/test-avatar.png");
		Files.createDirectories(avatar.getParent());
		Files.write(avatar, PNG_BYTES);

		mockMvc.perform(get(ApiRoutes.API_USER_AVATAR_IMAGE_BASE + "/test-avatar.png")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG));
	}

	private Jwt jwtToken() {
		return Jwt.withTokenValue("access-token").header("alg", "HS256").subject(USER_ID.toString())
				.claim("email", "test@gmail.com").claim("username", "test_user").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(3600)).build();
	}
}
