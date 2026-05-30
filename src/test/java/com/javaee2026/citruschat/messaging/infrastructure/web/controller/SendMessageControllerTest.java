package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaee2026.citruschat.messaging.application.results.SendMessageResult;
import com.javaee2026.citruschat.messaging.application.usecases.SendMessageUseCase;
import com.javaee2026.citruschat.messaging.domain.model.Message;
import com.javaee2026.citruschat.messaging.domain.model.MessageDevicePayload;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.DeviceId;
import com.javaee2026.citruschat.shared.domain.valueobjects.MessageId;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SendMessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@MockitoBean
	private SendMessageUseCase sendMessageUseCase;

	@Test
	void shouldSendMessage() throws Exception {
		Jwt jwt = Jwt.withTokenValue("access-token").header("alg", "HS256")
				.subject("91ae5825-9096-4c74-9447-1bf03004c36b").claim("email", "test@gmail.com")
				.claim("username", "test_user").issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(3600))
				.build();

		UUID chatRoomId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID senderDeviceId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID messageId = UUID.fromString("55555555-5555-5555-5555-555555555555");

		Message message = mock(Message.class);

		when(message.getId()).thenReturn(new MessageId(messageId));
		when(message.getChatRoomId()).thenReturn(new ChatRoomId(chatRoomId));
		when(message.getSenderDeviceId()).thenReturn(new DeviceId(senderDeviceId));
		when(message.getCreatedAt()).thenReturn(Instant.now());

		when(sendMessageUseCase.execute(any()))
				.thenReturn(new SendMessageResult(message, List.<MessageDevicePayload>of()));

		Map<String, Object> body = new java.util.LinkedHashMap<>();
		body.put("chatRoomId", chatRoomId.toString());
		body.put("senderDeviceId", senderDeviceId.toString());
		body.put("replyToMessageId", null);
		body.put("payloads",
				List.of(Map.of("targetDeviceId", "33333333-3333-3333-3333-333333333333", "encryptedPayload",
						"encrypted-for-device-1"),
						Map.of("targetDeviceId", "44444444-4444-4444-4444-444444444444", "encryptedPayload",
								"encrypted-for-device-2")));

		mockMvc.perform(post(ApiRoutes.API_CHAT_MESSAGES, chatRoomId).with(jwt().jwt(jwt))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.MESSAGE_SENT_SUCCESS)))
				.andExpect(jsonPath("$.data.sent", is(true)));

		verify(sendMessageUseCase).execute(any());
	}
}
