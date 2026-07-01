package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.usecases.GetCurrentUserDevicesUseCase;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class CurrentUserDevicesControllerTest {

	private static final String PUBLIC_KEY = "BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GetCurrentUserDevicesUseCase getCurrentUserDevicesUseCase;

	@Test
	void shouldReturnCurrentUserDevices() throws Exception {
		UUID userId = UUID.fromString("91ae5825-9096-4c74-9447-1bf03004c36b");
		UUID webDeviceId = UUID.fromString("6e85d451-50bc-4937-8602-8a85c5aa0b37");
		UUID mobileDeviceId = UUID.fromString("4f127a2c-6256-4f2d-8cc9-f937e8c38334");
		Instant webLastSeen = Instant.parse("2026-06-30T11:15:30Z");
		Instant mobileLastSeen = Instant.parse("2026-06-30T10:15:30Z");

		Jwt jwt = Jwt.withTokenValue("access-token").header("alg", "HS256").subject(userId.toString())
				.claim("email", "test@gmail.com").claim("username", "test_user").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(3600)).build();

		when(getCurrentUserDevicesUseCase.execute(userId)).thenReturn(List.of(
				UserDevice.reconstitute(new DeviceId(webDeviceId), new UserId(userId), new PublicKey(PUBLIC_KEY),
						"Chrome on Windows", DeviceType.WEB, null, webLastSeen, webLastSeen.minusSeconds(60), null),
				UserDevice.reconstitute(new DeviceId(mobileDeviceId), new UserId(userId), new PublicKey(PUBLIC_KEY),
						"Pixel 8", DeviceType.MOBILE, null, mobileLastSeen, mobileLastSeen.minusSeconds(60), null)));

		mockMvc.perform(get(ApiRoutes.API_AUTH_DEVICES).with(jwt().jwt(jwt))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.USER_DEVICES_RETRIEVED_SUCCESS)))
				.andExpect(jsonPath("$.data[0].id", is(webDeviceId.toString())))
				.andExpect(jsonPath("$.data[0].public_key", is(PUBLIC_KEY)))
				.andExpect(jsonPath("$.data[0].device_name", is("Chrome on Windows")))
				.andExpect(jsonPath("$.data[0].device_type", is("WEB")))
				.andExpect(jsonPath("$.data[0].last_seen", is(webLastSeen.toString())))
				.andExpect(jsonPath("$.data[1].id", is(mobileDeviceId.toString())))
				.andExpect(jsonPath("$.data[1].device_name", is("Pixel 8")))
				.andExpect(jsonPath("$.data[1].device_type", is("MOBILE")));
	}
}