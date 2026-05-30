package com.javaee2026.citruschat.messaging.infrastructure.configuration;

import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatParticipantRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatPermissionRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.usecases.*;
import com.javaee2026.citruschat.messaging.domain.factory.*;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatPermissionMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoleMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.ChatRoomMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.mapper.MessageMapper;
import com.javaee2026.citruschat.messaging.infrastructure.persistence.jpa.repository.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingBeansConfiguration {

	@Bean
	public MessageFactory messageFactory() {
		return new MessageFactory();
	}

	@Bean
	public MessageDevicePayloadFactory messageDevicePayloadFactory() {
		return new MessageDevicePayloadFactory();
	}

	@Bean
	public MessageMapper messageMapper(MessageFactory messageFactory) {
		return new MessageMapper(messageFactory);
	}

	@Bean
	public IMessageRepository messageRepository(SpringDataMessageRepository messageRepository,
			SpringDataMessageDevicePayloadRepository payloadRepository, MessageMapper messageMapper) {
		return new JpaMessageRepositoryAdapter(messageRepository, payloadRepository, messageMapper);
	}

	@Bean
	public SendMessageUseCase sendMessageUseCase(MessageFactory messageFactory, IMessageRepository messageRepository,
			MessageDevicePayloadFactory payloadFactory) {
		return new SendMessageUseCase(messageRepository, messageFactory, payloadFactory);
	}

	@Bean
	public ChatPermissionFactory chatPermissionFactory() {
		return new ChatPermissionFactory();
	}

	@Bean
	public ChatPermissionMapper chatPermissionMapper(ChatPermissionFactory chatPermissionFactory) {
		return new ChatPermissionMapper(chatPermissionFactory);
	}

	@Bean
	public ChatRoleMapper chatRoleMapper(ChatPermissionMapper chatPermissionMapper) {
		return new ChatRoleMapper(chatPermissionMapper);
	}

	@Bean
	public ChatRoomFactory chatRoomFactory() {
		return new ChatRoomFactory();
	}

	@Bean
	public ChatRoomMapper chatRoomMapper(ChatRoomFactory chatRoomFactory, ChatRoleMapper chatRoleMapper) {
		return new ChatRoomMapper(chatRoomFactory, chatRoleMapper);
	}

	@Bean
	public IChatRoomRepository chatRoomRepository(SpringDataChatRoomRepository springDataChatRoomRepository,
			ChatRoomMapper chatRoomMapper) {
		return new JpaChatRoomRepositoryAdapter(springDataChatRoomRepository, chatRoomMapper);
	}

	@Bean
	public IChatPermissionRepository chatPermissionRepository(
			SpingDataChatPermissionRepositoryAdapter chatPermissionRepository,
			ChatPermissionMapper chatPermissionMapper) {
		return new JpaChatPermissionRepositoryAdapter(chatPermissionRepository, chatPermissionMapper);
	}

	@Bean
	public CreateChatRoomUseCase createChatRoomUseCase(IChatRoomRepository chatRoomRepository,
			ChatRoomFactory chatRoomFactory, IUserRepository userRepository,
			IChatPermissionRepository permissionRepository) {
		return new CreateChatRoomUseCase(chatRoomRepository, chatRoomFactory, userRepository, permissionRepository);
	}

	@Bean
	public ValidateChatParticipantUseCase validateChatParticipantUseCase(
			IChatParticipantRepository chatParticipantRepository) {
		return new ValidateChatParticipantUseCase(chatParticipantRepository);
	}

	@Bean
	public GetCurrentUserChatRoomsUseCase getCurrentUserChatRoomsUseCase(IChatRoomRepository chatRoomRepository) {
		return new GetCurrentUserChatRoomsUseCase(chatRoomRepository);
	}

	@Bean
	public GetChatRoomMessagesUseCase getChatRoomMessagesUseCase(IMessageRepository messageRepository,
			ValidateChatParticipantUseCase validateChatParticipantUseCase) {
		return new GetChatRoomMessagesUseCase(messageRepository, validateChatParticipantUseCase);
	}

	@Bean
	public IChatParticipantRepository chatParticipantRepository(
			SpringDataChatParticipantRepository springDataChatParticipantRepository) {
		return new JpaChatParticipantRepositoryAdapter(springDataChatParticipantRepository);
	}
}
