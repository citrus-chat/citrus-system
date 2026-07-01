package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.commands.UpdateParticipantRolesCommand;
import com.javaee2026.citruschat.messaging.application.exceptions.ChatParticipantRolesException;
import com.javaee2026.citruschat.messaging.application.results.UpdateParticipantRolesResult;
import com.javaee2026.citruschat.messaging.application.usecases.UpdateParticipantRolesUseCase;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class UpdateParticipantRolesControllerTest {

	private final UUID chatRoomId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000001");
	private final UUID participantId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000002");
	private final UUID requesterUserId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000003");
	private final UUID targetUserId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000004");
	private final UUID roleId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000005");
	private final UUID permissionId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000006");

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UpdateParticipantRolesUseCase updateParticipantRolesUseCase;

	@Test
	void shouldReturnUpdatedRoles() throws Exception {
		when(updateParticipantRolesUseCase.execute(any())).thenReturn(result());

		mockMvc.perform(
				patch(ApiRoutes.API_CHAT_ROOM_PARTICIPANT_ROLES, chatRoomId, participantId).with(jwt().jwt(jwtToken()))
						.contentType(MediaType.APPLICATION_JSON).content("{\"roleIds\":[\"" + roleId + "\"]}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.PARTICIPANT_ROLES_UPDATED_SUCCESS)))
				.andExpect(jsonPath("$.data.participantId", is(participantId.toString())))
				.andExpect(jsonPath("$.data.chatRoomId", is(chatRoomId.toString())))
				.andExpect(jsonPath("$.data.userId", is(targetUserId.toString())))
				.andExpect(jsonPath("$.data.roleIds[0]", is(roleId.toString())))
				.andExpect(jsonPath("$.data.permissions[0].code", is(ChatPermissionList.CAN_MODIFY_ROLE)));

		ArgumentCaptor<UpdateParticipantRolesCommand> captor = ArgumentCaptor
				.forClass(UpdateParticipantRolesCommand.class);
		verify(updateParticipantRolesUseCase).execute(captor.capture());
		assertEquals(chatRoomId, captor.getValue().chatRoomId().value());
		assertEquals(participantId, captor.getValue().participantId().value());
		assertEquals(requesterUserId, captor.getValue().requesterUserId().value());
		assertEquals(List.of(new RoleId(roleId)), captor.getValue().roleIds());
	}

	@Test
	void shouldReturnUnauthorizedWithoutToken() throws Exception {
		mockMvc.perform(patch(ApiRoutes.API_CHAT_ROOM_PARTICIPANT_ROLES, chatRoomId, participantId)
				.contentType(MediaType.APPLICATION_JSON).content("{\"roleIds\":[\"" + roleId + "\"]}"))
				.andExpect(status().isUnauthorized());

		verifyNoInteractions(updateParticipantRolesUseCase);
	}

	@Test
	void shouldReturnForbiddenWhenUseCaseRejectsPermission() throws Exception {
		when(updateParticipantRolesUseCase.execute(any()))
				.thenThrow(new ChatParticipantRolesException(ErrorCode.FORBIDDEN, "Forbidden"));

		mockMvc.perform(validPatch()).andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errorCode", is(ErrorCode.FORBIDDEN.name())));
	}

	@Test
	void shouldReturnNotFoundWhenParticipantDoesNotExist() throws Exception {
		when(updateParticipantRolesUseCase.execute(any())).thenThrow(new ChatParticipantRolesException(
				ErrorCode.CHAT_PARTICIPANT_NOT_FOUND, "Active participant not found"));

		mockMvc.perform(validPatch()).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorCode", is(ErrorCode.CHAT_PARTICIPANT_NOT_FOUND.name())));
	}

	@Test
	void shouldReturnConflictWhenLastAdminWouldBeRemoved() throws Exception {
		when(updateParticipantRolesUseCase.execute(any()))
				.thenThrow(new ChatParticipantRolesException(ErrorCode.CHAT_RULE_CONFLICT, "Cannot remove last admin"));

		mockMvc.perform(validPatch()).andExpect(status().isConflict())
				.andExpect(jsonPath("$.errorCode", is(ErrorCode.CHAT_RULE_CONFLICT.name())));
	}

	@Test
	void shouldReturnBadRequestWithInvalidBody() throws Exception {
		mockMvc.perform(patch(ApiRoutes.API_CHAT_ROOM_PARTICIPANT_ROLES, chatRoomId, participantId)
				.with(jwt().jwt(jwtToken())).contentType(MediaType.APPLICATION_JSON).content("{\"roleIds\":[]}"))
				.andExpect(status().isBadRequest());

		verifyNoInteractions(updateParticipantRolesUseCase);
	}

	private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder validPatch() {
		return patch(ApiRoutes.API_CHAT_ROOM_PARTICIPANT_ROLES, chatRoomId, participantId).with(jwt().jwt(jwtToken()))
				.contentType(MediaType.APPLICATION_JSON).content("{\"roleIds\":[\"" + roleId + "\"]}");
	}

	private Jwt jwtToken() {
		return Jwt.withTokenValue("token").header("alg", "HS256").subject(requesterUserId.toString())
				.issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(3600)).build();
	}

	private UpdateParticipantRolesResult result() {
		ChatPermission permission = new ChatPermission(new PermissionId(permissionId),
				ChatPermissionList.CAN_MODIFY_ROLE, "Can modify roles");
		return new UpdateParticipantRolesResult(new ParticipantId(participantId), new ChatRoomId(chatRoomId),
				new UserId(targetUserId), List.of(new RoleId(roleId)), Set.of(permission));
	}
}
