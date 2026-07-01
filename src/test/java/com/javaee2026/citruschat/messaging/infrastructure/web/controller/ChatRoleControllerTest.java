package com.javaee2026.citruschat.messaging.infrastructure.web.controller;

import com.javaee2026.citruschat.messaging.application.exceptions.ChatRoleException;
import com.javaee2026.citruschat.messaging.application.results.CreateChatRoleResult;
import com.javaee2026.citruschat.messaging.application.results.GetAvailableChatPermissionsResult;
import com.javaee2026.citruschat.messaging.application.results.GetChatRoleResult;
import com.javaee2026.citruschat.messaging.application.results.GetChatRolesResult;
import com.javaee2026.citruschat.messaging.application.results.UpdateChatRoleResult;
import com.javaee2026.citruschat.messaging.application.usecases.CreateChatRoleUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.DeleteChatRoleUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.GetAvailableChatPermissionsUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.GetChatRoleUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.GetChatRolesUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.UpdateChatRoleUseCase;
import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.domain.errors.ErrorCode;
import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class ChatRoleControllerTest {

	private final UUID chatRoomId = UUID.fromString("dddddddd-0000-0000-0000-000000000001");
	private final UUID roleId = UUID.fromString("dddddddd-0000-0000-0000-000000000002");
	private final UUID requesterUserId = UUID.fromString("dddddddd-0000-0000-0000-000000000003");
	private final UUID permissionId = UUID.fromString("dddddddd-0000-0000-0000-000000000004");
	private final UUID replacementRoleId = UUID.fromString("dddddddd-0000-0000-0000-000000000005");

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GetChatRolesUseCase getChatRolesUseCase;

	@MockitoBean
	private GetChatRoleUseCase getChatRoleUseCase;

	@MockitoBean
	private CreateChatRoleUseCase createChatRoleUseCase;

	@MockitoBean
	private UpdateChatRoleUseCase updateChatRoleUseCase;

	@MockitoBean
	private DeleteChatRoleUseCase deleteChatRoleUseCase;

	@MockitoBean
	private GetAvailableChatPermissionsUseCase getAvailableChatPermissionsUseCase;

	@Test
	void getRolesReturnsOk() throws Exception {
		when(getChatRolesUseCase.execute(any())).thenReturn(new GetChatRolesResult(List.of(role())));

		mockMvc.perform(get(ApiRoutes.API_CHAT_ROOM_ROLES, chatRoomId).with(jwt().jwt(jwtToken())))
				.andExpect(status().isOk()).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.CHAT_ROLES_RETRIEVED_SUCCESS)))
				.andExpect(jsonPath("$.data.roles[0].id", is(roleId.toString())));
	}

	@Test
	void getRoleReturnsOk() throws Exception {
		when(getChatRoleUseCase.execute(any())).thenReturn(new GetChatRoleResult(role()));

		mockMvc.perform(get(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID, chatRoomId, roleId).with(jwt().jwt(jwtToken())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.CHAT_ROLE_RETRIEVED_SUCCESS)))
				.andExpect(jsonPath("$.data.id", is(roleId.toString())));
	}

	@Test
	void postRoleReturnsCreated() throws Exception {
		when(createChatRoleUseCase.execute(any())).thenReturn(new CreateChatRoleResult(role()));

		mockMvc.perform(post(ApiRoutes.API_CHAT_ROOM_ROLES, chatRoomId).with(jwt().jwt(jwtToken()))
				.contentType(MediaType.APPLICATION_JSON).content(validBody())).andExpect(status().isCreated())
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.CHAT_ROLE_CREATED_SUCCESS)))
				.andExpect(jsonPath("$.data.name", is("Moderator")));
	}

	@Test
	void patchRoleReturnsOk() throws Exception {
		when(updateChatRoleUseCase.execute(any())).thenReturn(new UpdateChatRoleResult(role()));

		mockMvc.perform(patch(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID, chatRoomId, roleId).with(jwt().jwt(jwtToken()))
				.contentType(MediaType.APPLICATION_JSON).content(validBody())).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.CHAT_ROLE_UPDATED_SUCCESS)));
	}

	@Test
	void deleteRoleReturnsOk() throws Exception {
		mockMvc.perform(delete(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID, chatRoomId, roleId).with(jwt().jwt(jwtToken()))
				.param("replacementRoleId", replacementRoleId.toString())).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.CHAT_ROLE_DELETED_SUCCESS)));
	}

	@Test
	void getPermissionsReturnsOk() throws Exception {
		when(getAvailableChatPermissionsUseCase.execute(any()))
				.thenReturn(new GetAvailableChatPermissionsResult(List.of(permission())));

		mockMvc.perform(get(ApiRoutes.API_CHAT_PERMISSIONS).with(jwt().jwt(jwtToken()))).andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.CHAT_PERMISSIONS_RETRIEVED_SUCCESS)))
				.andExpect(jsonPath("$.data.permissions[0].code", is(ChatPermissionList.CAN_SEND_MESSAGE)));
	}

	@Test
	void postRoleWithoutTokenReturnsUnauthorized() throws Exception {
		mockMvc.perform(post(ApiRoutes.API_CHAT_ROOM_ROLES, chatRoomId).contentType(MediaType.APPLICATION_JSON)
				.content(validBody())).andExpect(status().isUnauthorized());

		verifyNoInteractions(createChatRoleUseCase);
	}

	@Test
	void forbiddenUseCaseReturnsForbidden() throws Exception {
		when(createChatRoleUseCase.execute(any())).thenThrow(new ChatRoleException(ErrorCode.FORBIDDEN, "Forbidden"));

		mockMvc.perform(post(ApiRoutes.API_CHAT_ROOM_ROLES, chatRoomId).with(jwt().jwt(jwtToken()))
				.contentType(MediaType.APPLICATION_JSON).content(validBody())).andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errorCode", is(ErrorCode.FORBIDDEN.name())));
	}

	@Test
	void missingChatRoomReturnsNotFound() throws Exception {
		when(getChatRolesUseCase.execute(any()))
				.thenThrow(new ChatRoleException(ErrorCode.CHATROOM_NOT_FOUND, "Chat room not found"));

		mockMvc.perform(get(ApiRoutes.API_CHAT_ROOM_ROLES, chatRoomId).with(jwt().jwt(jwtToken())))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorCode", is(ErrorCode.CHATROOM_NOT_FOUND.name())));
	}

	@Test
	void missingRoleReturnsNotFound() throws Exception {
		when(updateChatRoleUseCase.execute(any()))
				.thenThrow(new ChatRoleException(ErrorCode.CHATROLE_NOT_FOUND, "Chat role not found"));

		mockMvc.perform(patch(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID, chatRoomId, roleId).with(jwt().jwt(jwtToken()))
				.contentType(MediaType.APPLICATION_JSON).content(validBody())).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorCode", is(ErrorCode.CHATROLE_NOT_FOUND.name())));
	}

	@Test
	void conflictReturnsConflict() throws Exception {
		when(deleteChatRoleUseCase.execute(any()))
				.thenThrow(new ChatRoleException(ErrorCode.CHAT_RULE_CONFLICT, "Cannot delete last admin"));

		mockMvc.perform(delete(ApiRoutes.API_CHAT_ROOM_ROLE_BY_ID, chatRoomId, roleId).with(jwt().jwt(jwtToken())))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.errorCode", is(ErrorCode.CHAT_RULE_CONFLICT.name())));
	}

	@Test
	void invalidBodyReturnsBadRequest() throws Exception {
		mockMvc.perform(post(ApiRoutes.API_CHAT_ROOM_ROLES, chatRoomId).with(jwt().jwt(jwtToken()))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"\",\"priority\":50,\"permissionIds\":[]}")).andExpect(status().isBadRequest());

		verifyNoInteractions(createChatRoleUseCase);
	}

	@Test
	void springDocExposesRoleEndpointsAndRoleSchemas() throws Exception {
		mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
				.andExpect(jsonPath("$.paths['/api/v1/chatroom/{chatroomId}/roles'].get").exists())
				.andExpect(jsonPath("$.paths['/api/v1/chatroom/{chatroomId}/roles'].post").exists())
				.andExpect(jsonPath("$.paths['/api/v1/chatroom/{chatroomId}/roles/{roleId}'].get").exists())
				.andExpect(jsonPath("$.paths['/api/v1/chatroom/{chatroomId}/roles/{roleId}'].patch").exists())
				.andExpect(jsonPath("$.paths['/api/v1/chatroom/{chatroomId}/roles/{roleId}'].delete").exists())
				.andExpect(jsonPath("$.paths['/api/v1/chatroom/permissions'].get").exists())
				.andExpect(jsonPath("$.components.schemas.ChatRoomResponse.properties.roles").exists())
				.andExpect(jsonPath("$.components.schemas.CreateChatRoomResponse.properties.roles").exists());
	}
	private String validBody() {
		return "{\"name\":\"Moderator\",\"priority\":50,\"permissionIds\":[\"" + permissionId + "\"]}";
	}

	private Jwt jwtToken() {
		return Jwt.withTokenValue("token").header("alg", "HS256").subject(requesterUserId.toString())
				.issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(3600)).build();
	}

	private ChatRole role() {
		return ChatRole.reconstitute(new RoleId(roleId), new ChatRoomId(chatRoomId), Set.of(permission()), "Moderator",
				50, Instant.now());
	}

	private ChatPermission permission() {
		return new ChatPermission(new PermissionId(permissionId), ChatPermissionList.CAN_SEND_MESSAGE,
				"Allows sending messages");
	}
}
