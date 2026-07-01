// package com.javaee2026.citruschat.messaging.application.usecases;

// import
// com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
// import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
// import com.javaee2026.citruschat.identity.domain.model.User;
// import
// com.javaee2026.citruschat.messaging.application.commands.SyncMessagesCommand;
// import
// com.javaee2026.citruschat.messaging.application.exceptions.ChatPermissionDeniedException;
// import
// com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
// import
// com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
// import
// com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
// import
// com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
// import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
// import com.javaee2026.citruschat.messaging.domain.factory.MessageFactory;
// import com.javaee2026.citruschat.messaging.domain.model.ChatParticipant;
// import com.javaee2026.citruschat.messaging.domain.model.ChatPermission;
// import com.javaee2026.citruschat.messaging.domain.model.ChatRole;
// import com.javaee2026.citruschat.messaging.domain.model.ChatRoom;
// import
// com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
// import
// com.javaee2026.citruschat.messaging.infrastructure.websocket.ports.IMessageRealtimeNotifier;
// import com.javaee2026.citruschat.shared.domain.valueobjects.ChatRoomId;
// import com.javaee2026.citruschat.shared.domain.valueobjects.ParticipantId;
// import com.javaee2026.citruschat.shared.domain.valueobjects.PermissionId;
// import com.javaee2026.citruschat.shared.domain.valueobjects.RoleId;
// import com.javaee2026.citruschat.shared.domain.valueobjects.UserId;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.Instant;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.Set;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// class MessagePermissionUseCaseTest {

// private final ChatRoomId chatRoomId = new
// ChatRoomId(UUID.fromString("eeeeeeee-0000-0000-0000-000000000101"));
// private final UserId creatorUserId = new
// UserId(UUID.fromString("eeeeeeee-0000-0000-0000-000000000102"));
// private final UserId requesterUserId = new
// UserId(UUID.fromString("eeeeeeee-0000-0000-0000-000000000103"));
// private final RoleId viewRoleId = new
// RoleId(UUID.fromString("eeeeeeee-0000-0000-0000-000000000104"));
// private final RoleId sendRoleId = new
// RoleId(UUID.fromString("eeeeeeee-0000-0000-0000-000000000105"));

// @Mock
// private IMessageRepository messageRepository;

// @Mock
// private IChatRoomRepository chatRoomRepository;

// @Mock
// private IChatParticipantRepository chatParticipantRepository;

// @Mock
// private IChatPermissionRepository chatPermissionRepository;

// @Mock
// private IUserDeviceRepository deviceRepository;

// @Mock
// private IUserRepository userRepository;

// @Mock
// private MessageFactory messageFactory;

// @Mock
// private IMessageRealtimeNotifier realtimeNotifier;

// @Test
// void participantWithOnlyViewMessagePermissionCanSyncMessages() {
// ChatRoom room = room(role(viewRoleId, "VIEWER",
// ChatPermissionList.CAN_VIEW_MESSAGE));
// when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
// effectiveRequesterPermissions(ChatPermissionList.CAN_VIEW_MESSAGE);
// when(messageRepository.findMessagesAfter(chatRoomId, null,
// 100)).thenReturn(List.of());

// assertDoesNotThrow(() -> syncUseCase().execute(new
// SyncMessagesCommand(chatRoomId, requesterUserId, null)));

// verify(messageRepository).findMessagesAfter(chatRoomId, null, 100);
// }

// @Test
// void participantWithoutViewMessagePermissionCannotSyncMessages() {
// ChatRoom room = room(role(sendRoleId, "SENDER",
// ChatPermissionList.CAN_SEND_MESSAGE));
// when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
// effectiveRequesterPermissions(ChatPermissionList.CAN_SEND_MESSAGE);

// assertThrows(ChatPermissionDeniedException.class,
// () -> syncUseCase().execute(new SyncMessagesCommand(chatRoomId,
// requesterUserId, null)));

// verify(messageRepository, never()).findMessagesAfter(chatRoomId, null, 100);
// }

// @Test
// void participantWithViewMessagePermissionCanListMessages() {
// ChatRoom room = room(role(viewRoleId, "VIEWER",
// ChatPermissionList.CAN_VIEW_MESSAGE));
// when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
// effectiveRequesterPermissions(ChatPermissionList.CAN_VIEW_MESSAGE);
// when(messageRepository.findMessagesByChatRoomId(chatRoomId, 0,
// 100)).thenReturn(List.of());

// assertDoesNotThrow(() -> getMessagesUseCase().execute(chatRoomId.value(),
// requesterUserId.value(), -1, 500));

// verify(messageRepository).findMessagesByChatRoomId(chatRoomId, 0, 100);
// }

// @Test
// void participantWithoutViewMessagePermissionCannotListMessages() {
// ChatRoom room = room(role(sendRoleId, "SENDER",
// ChatPermissionList.CAN_SEND_MESSAGE));
// when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(room));
// effectiveRequesterPermissions(ChatPermissionList.CAN_SEND_MESSAGE);

// assertThrows(ChatPermissionDeniedException.class,
// () -> getMessagesUseCase().execute(chatRoomId.value(),
// requesterUserId.value(), 0, 50));

// verify(messageRepository, never()).findMessagesByChatRoomId(chatRoomId, 0,
// 50);
// }

// @Test
// void participantWithOnlyViewMessagePermissionCannotSendMessages() {
// ChatRoom room = room(role(viewRoleId, "VIEWER",
// ChatPermissionList.CAN_VIEW_MESSAGE));
// User sender = user(requesterUserId);
// effectiveRequesterPermissions(ChatPermissionList.CAN_VIEW_MESSAGE);

// assertThrows(ChatPermissionDeniedException.class, () ->
// sendUseCase().validateSenderPermissions(room, sender));
// }

// @Test
// void participantWithSendMessagePermissionCanSendMessages() {
// ChatRoom room = room(role(sendRoleId, "SENDER",
// ChatPermissionList.CAN_SEND_MESSAGE));
// User sender = user(requesterUserId);
// effectiveRequesterPermissions(ChatPermissionList.CAN_SEND_MESSAGE);

// assertDoesNotThrow(() -> sendUseCase().validateSenderPermissions(room,
// sender));
// }

// private SyncMessagesUseCase syncUseCase() {
// return new SyncMessagesUseCase(messageRepository, authorizationService());
// }

// private SendMessageUseCase sendUseCase() {
// return new SendMessageUseCase(deviceRepository, userRepository,
// chatRoomRepository, messageRepository,
// messageFactory, realtimeNotifier, authorizationService());
// }

// private GetChatRoomMessagesUseCase getMessagesUseCase() {
// return new GetChatRoomMessagesUseCase(messageRepository,
// authorizationService());
// }

// private ChatPermissionAuthorizationService authorizationService() {
// return new ChatPermissionAuthorizationService(chatRoomRepository,
// chatParticipantRepository,
// chatPermissionRepository);
// }

// private void effectiveRequesterPermissions(String permissionCode) {
// ChatParticipant participant = participant(
// ChatPermissionList.CAN_VIEW_MESSAGE.equals(permissionCode) ? viewRoleId :
// sendRoleId);
// when(chatParticipantRepository.findActiveByChatRoomIdAndUserId(chatRoomId,
// requesterUserId))
// .thenReturn(Optional.of(participant));
// when(chatPermissionRepository.findPermissionsByChatRoomAndParticipant(chatRoomId,
// participant.getId()))
// .thenReturn(Set.of(permission(permissionCode)));
// }

// private User user(UserId userId) {
// User user = mock(User.class);
// when(user.getId()).thenReturn(userId);
// return user;
// }

// private ChatRoom room(ChatRole role) {
// return new ChatRoom(chatRoomId, ChatRoomType.GROUP, "Group", creatorUserId,
// List.of(participant(role.getId())),
// Map.of(role.getName(), role), Instant.now(), null, null);
// }

// private ChatParticipant participant(RoleId roleId) {
// return ChatParticipant.reconstitute(new
// ParticipantId(UUID.fromString("eeeeeeee-0000-0000-0000-000000000106")),
// chatRoomId, requesterUserId, List.of(roleId), Instant.now(), null, null);
// }

// private ChatRole role(RoleId roleId, String name, String permissionCode) {
// return ChatRole.reconstitute(roleId, chatRoomId,
// Set.of(permission(permissionCode)), name, 10, Instant.now());
// }

// private ChatPermission permission(String code) {
// UUID id = ChatPermissionList.CAN_VIEW_MESSAGE.equals(code)
// ? UUID.fromString("eeeeeeee-0000-0000-0000-000000000107")
// : UUID.fromString("eeeeeeee-0000-0000-0000-000000000108");
// return new ChatPermission(new PermissionId(id), code, code);
// }
// }
