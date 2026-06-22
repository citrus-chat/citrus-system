package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.usecases.GetCurrentUserUseCase;
import com.javaee2026.citruschat.identity.infrastructure.security.jwt.JwtService;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtSecurityIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtService jwtService;

	@MockitoBean
	private GetCurrentUserUseCase getCurrentUserUseCase;

	@Test
	void shouldReturnUnauthorizedWithoutToken() throws Exception {
		mockMvc.perform(get(ApiRoutes.API_AUTH_ME)).andExpect(status().isUnauthorized());
	}

	@Test
	void shouldAllowAccessWithValidToken() throws Exception {
		UUID userId = UUID.fromString("91ae5825-9096-4c74-9447-1bf03004c36b");
		String token = jwtService.generateToken(userId.toString(), "91ae5825-9096-4c74-7654-1bf03004c36b",
				"test@gmail.com", "test_test");

		when(getCurrentUserUseCase.execute(any()))
				.thenReturn(new UserResult(userId, "test_test", "test@gmail.com", true, null));

		mockMvc.perform(get(ApiRoutes.API_AUTH_ME).header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk());
	}

	@Test
	void shouldReturnUnauthorizedWithInvalidToken() throws Exception {
		mockMvc.perform(get(ApiRoutes.API_AUTH_ME).header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
				.andExpect(status().isUnauthorized());
	}
}
