package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.results.CreateWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.results.ConfirmWebLoginTokenResult;
import com.javaee2026.citruschat.identity.application.usecases.CreateWebLoginTokenUseCase;
import com.javaee2026.citruschat.identity.application.usecases.ConfirmWebLoginTokenUseCase;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebLoginTokenControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CreateWebLoginTokenUseCase createWebLoginTokenUseCase;

	@MockitoBean
	private ConfirmWebLoginTokenUseCase confirmWebLoginTokenUseCase;

	@Test
	void shouldCreateWebLoginTokenWithoutAuthentication() throws Exception {
		UUID webDeviceId = UUID.fromString("91ae5825-9096-4c74-9447-1bf03004c36b");
		Instant expiresAt = Instant.parse("2026-07-01T14:30:00Z");

		when(createWebLoginTokenUseCase.execute(any()))
				.thenReturn(new CreateWebLoginTokenResult("temporary-token", webDeviceId, expiresAt,
						"temporary-token"));

		mockMvc.perform(post(ApiRoutes.API_WEB_LOGIN_TOKENS).contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "webDeviceId": "91ae5825-9096-4c74-9447-1bf03004c36b",
						  "deviceName": "Browser",
						  "publicKey": "public-key"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.WEB_LOGIN_TOKEN_CREATED)))
				.andExpect(jsonPath("$.data.token", is("temporary-token")))
				.andExpect(jsonPath("$.data.web_device_id", is(webDeviceId.toString())))
				.andExpect(jsonPath("$.data.expires_at", is(expiresAt.toString())))
				.andExpect(jsonPath("$.data.qr_payload", is("temporary-token")));
	}

	@Test
	void shouldConfirmWebLoginTokenWithAuthenticatedUser() throws Exception {
		UUID userId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
		UUID webDeviceId = UUID.fromString("91ae5825-9096-4c74-9447-1bf03004c36b");
		Jwt authentication = Jwt.withTokenValue("mobile-token").header("alg", "HS256").subject(userId.toString())
				.claim("deviceId", "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(3600)).build();

		when(confirmWebLoginTokenUseCase.execute(any()))
				.thenReturn(new ConfirmWebLoginTokenResult(userId, webDeviceId));

		mockMvc.perform(post(ApiRoutes.API_WEB_LOGIN_TOKENS_CONFIRM).with(jwt().jwt(authentication))
				.contentType(MediaType.APPLICATION_JSON).content("""
						{
						  "token": "temporary-token"
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.WEB_LOGIN_TOKEN_CONFIRMED)))
				.andExpect(jsonPath("$.data.user_id", is(userId.toString())))
				.andExpect(jsonPath("$.data.web_device_id", is(webDeviceId.toString())));
	}

	@Test
	void shouldRejectConfirmWebLoginTokenWithoutAuthentication() throws Exception {
		mockMvc.perform(post(ApiRoutes.API_WEB_LOGIN_TOKENS_CONFIRM).contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "token": "temporary-token"
						}
						"""))
				.andExpect(status().isUnauthorized());
	}
}
