package com.javaee2026.citruschat.messaging.infrastructure.configuration;

import com.javaee2026.citruschat.identity.application.ports.IUserRepository;
import com.javaee2026.citruschat.messaging.application.ports.IChatRoomRepository;
import com.javaee2026.citruschat.messaging.application.ports.IMessageRepository;
import com.javaee2026.citruschat.messaging.application.usecases.CreateChatRoomUseCase;
import com.javaee2026.citruschat.messaging.application.usecases.SendMessageUseCase;
import com.javaee2026.citruschat.messaging.domain.factory.*;
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
	public ChatRoomFactory chatRoomFactory() {
		return new ChatRoomFactory();
	}

	@Bean
	public ChatRoomMapper chatRoomMapper(ChatRoomFactory chatRoomFactory) {
		return new ChatRoomMapper(chatRoomFactory);
	}

	@Bean
	public IChatRoomRepository chatRoomRepository(SpringDataChatRoomRepository springDataChatRoomRepository,
			ChatRoomMapper chatRoomMapper) {
		return new JpaChatRoomRepositoryAdapter(springDataChatRoomRepository, chatRoomMapper);
	}

	@Bean
	public CreateChatRoomUseCase createChatRoomUseCase(IChatRoomRepository chatRoomRepository,
			ChatRoomFactory chatRoomFactory, IUserRepository userRepository) {
		return new CreateChatRoomUseCase(chatRoomRepository, chatRoomFactory, userRepository);
	}
}
