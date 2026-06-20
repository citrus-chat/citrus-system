package com.javaee2026.citruschat.messaging.application.usecases;

import com.javaee2026.citruschat.identity.application.ports.IUserDeviceRepository;
import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.model.User;
import com.javaee2026.citruschat.identity.domain.model.UserDevice;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;
import com.javaee2026.citruschat.messaging.application.commands.MessageDevicePayloadCommand;
import com.javaee2026.citruschat.messaging.application.commands.SendMessageCommand;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.results.SendMessageResult;
import com.javaee2026.citruschat.messaging.domain.enums.ChatRoomType;
import com.javaee2026.citruschat.messaging.domain.factory.MessageDevicePayloadFactory;
import com.javaee2026.citruschat.messaging.domain.factory.MessageFactory;
import com.javaee2026.citruschat.messaging.domain.model.*;
import com.javaee2026.citruschat.messaging.domain.policy.permissions.ChatPermissionList;
import com.javaee2026.citruschat.shared.domain.valueobjects.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SendMessageUseCaseTest {

	private final IUserRepository userRepository = mock(IUserRepository.class);

	private final IUserDeviceRepository deviceRepository = mock(IUserDeviceRepository.class);

	private final IChatRoomRepository chatRoomRepository = mock(IChatRoomRepository.class);

	private final IMessageRepository messageRepository = mock(IMessageRepository.class);

	private final SendMessageUseCase useCase = new SendMessageUseCase(deviceRepository, userRepository,
			chatRoomRepository, messageRepository, new MessageFactory(), new MessageDevicePayloadFactory());

	@Test
	void shouldSendMessageSuccessfully() {

		UUID senderUserId = UUID.randomUUID();
		UUID senderDeviceId = UUID.randomUUID();
		UUID targetDeviceId = UUID.randomUUID();
		UUID chatRoomId = UUID.randomUUID();
		String senderPublicKey = "l7sW4w4Qj0f0H5qKJr9z3tD7NwV6g1sYc2mF8rLpQa8=";
		String targetPublicKey = "AQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHyA=";

		User sender = mock(User.class);
		when(sender.getId()).thenReturn(new UserId(senderUserId));

		UserDevice senderDevice = createDevice(senderUserId, senderDeviceId, senderPublicKey);
		UserDevice receiverDevice = createDevice(UUID.randomUUID(), targetDeviceId, targetPublicKey);

		ChatRoom chatRoom = createValidChatRoom(senderUserId);

		when(userRepository.findById(any())).thenReturn(Optional.of(sender));

		when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));

		when(deviceRepository.findActiveByIdAndUserId(senderDeviceId, senderUserId))
				.thenReturn(Optional.of(senderDevice));

		when(deviceRepository.findAllByUserId(any())).thenReturn(List.of(senderDevice, receiverDevice));

		SendMessageCommand command = new SendMessageCommand(chatRoomId, senderUserId, senderDeviceId, null,
				List.of(new MessageDevicePayloadCommand(targetDeviceId, "encrypted-payload")));

		SendMessageResult result = useCase.execute(command);

		ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

		ArgumentCaptor<List<MessageDevicePayload>> payloadCaptor = ArgumentCaptor.forClass(List.class);

		verify(messageRepository).save(messageCaptor.capture(), payloadCaptor.capture());

		Message savedMessage = messageCaptor.getValue();

		assertNotNull(savedMessage);
		assertEquals(senderUserId, savedMessage.getSenderUserId().value());

		assertEquals(senderDeviceId, savedMessage.getSenderDeviceId().value());

		assertEquals(1, result.payloads().size());
	}

	@Test
	void shouldThrowWhenSenderDoesNotExist() {

		when(userRepository.findById(any())).thenReturn(Optional.empty());

		SendMessageCommand command = new SendMessageCommand(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
				null, List.of());

		assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));

		verify(messageRepository, never()).save(any(), any());
	}

	@Test
	void shouldThrowWhenChatRoomDoesNotExist() {

		UUID senderId = UUID.randomUUID();

		User sender = mock(User.class);
		when(sender.getId()).thenReturn(new UserId(senderId));

		when(userRepository.findById(any())).thenReturn(Optional.of(sender));

		when(chatRoomRepository.findById(any())).thenReturn(Optional.empty());

		SendMessageCommand command = new SendMessageCommand(UUID.randomUUID(), senderId, UUID.randomUUID(), null,
				List.of());

		assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
	}

	@Test
	void shouldThrowWhenSenderIsNotParticipant() {

		UUID senderId = UUID.randomUUID();

		User sender = mock(User.class);
		when(sender.getId()).thenReturn(new UserId(senderId));

		ChatRoom chatRoom = createChatRoomWithoutSender();

		when(userRepository.findById(any())).thenReturn(Optional.of(sender));

		when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));

		SendMessageCommand command = new SendMessageCommand(UUID.randomUUID(), senderId, UUID.randomUUID(), null,
				List.of());

		assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
	}

	@Test
	void shouldThrowWhenSenderDeviceDoesNotExist() {

		UUID senderUserId = UUID.randomUUID();
		UUID senderDeviceId = UUID.randomUUID();

		User sender = mock(User.class);
		when(sender.getId()).thenReturn(new UserId(senderUserId));

		ChatRoom chatRoom = createValidChatRoom(senderUserId);

		when(userRepository.findById(any())).thenReturn(Optional.of(sender));

		when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));

		when(deviceRepository.findActiveByIdAndUserId(senderDeviceId, senderUserId)).thenReturn(Optional.empty());

		SendMessageCommand command = new SendMessageCommand(UUID.randomUUID(), senderUserId, senderDeviceId, null,
				List.of());

		assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
	}

	@Test
	void shouldThrowWhenTargetDeviceIsInvalid() {

		UUID senderUserId = UUID.randomUUID();
		UUID senderDeviceId = UUID.randomUUID();
		String publicKey = "AQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHyA=";

		User sender = mock(User.class);
		when(sender.getId()).thenReturn(new UserId(senderUserId));

		UserDevice senderDevice = createDevice(senderUserId, senderDeviceId, publicKey);

		ChatRoom chatRoom = createValidChatRoom(senderUserId);

		when(userRepository.findById(any())).thenReturn(Optional.of(sender));

		when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));

		when(deviceRepository.findActiveByIdAndUserId(senderDeviceId, senderUserId))
				.thenReturn(Optional.of(senderDevice));

		when(deviceRepository.findAllByUserId(any())).thenReturn(List.of(senderDevice));

		SendMessageCommand command = new SendMessageCommand(UUID.randomUUID(), senderUserId, senderDeviceId, null,
				List.of(new MessageDevicePayloadCommand(UUID.randomUUID(), "encrypted")));

		assertThrows(IllegalArgumentException.class, () -> useCase.execute(command));
	}

	private ChatRoom createValidChatRoom(UUID senderUserId) {

		ChatRoomId roomId = ChatRoomId.newId();

		ChatPermission permission = new ChatPermission(PermissionId.newId(), ChatPermissionList.CAN_SEND_MESSAGE,
				"Can send");

		ChatRole role = ChatRole.create(roomId, Set.of(permission), "OWNER", 100);

		ChatParticipant participant = ChatParticipant.reconstitute(ParticipantId.newId(), roomId,
				new UserId(senderUserId), List.of(role.getId()), Instant.now(), null, null);

		Map<String, ChatRole> roles = new HashMap<>();
		roles.put(role.getName(), role);

		return new ChatRoom(roomId, ChatRoomType.GROUP, "Test Room", new UserId(senderUserId),
				new ArrayList<>(List.of(participant)), roles, Instant.now(), null, null);
	}

	private ChatRoom createChatRoomWithoutSender() {

		UUID otherUser = UUID.randomUUID();

		return createValidChatRoom(otherUser);
	}

	private UserDevice createDevice(UUID userId, UUID deviceId, String publicKey) {

		return UserDevice.reconstitute(new DeviceId(deviceId), new UserId(userId), new PublicKey(publicKey), "device",
				DeviceType.WEB, Instant.now(), Instant.now(), null);
	}
}
