package com.javaee2026.citruschat.messaging.infrastructure.websocket.configuration;

import com.javaee2026.citruschat.shared.domain.constants.ConfigConstants;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	private final StompJwtChannelInterceptor stompJwtChannelInterceptor;

	public WebSocketConfiguration(StompJwtChannelInterceptor stompJwtChannelInterceptor) {
		this.stompJwtChannelInterceptor = stompJwtChannelInterceptor;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker(ApiRoutes.WS_TOPIC_BASE, ApiRoutes.WS_QUEUE_BASE);
		config.setApplicationDestinationPrefixes(ApiRoutes.WS_APP_PREFIX);
		config.setUserDestinationPrefix(ApiRoutes.WS_USER_PREFIX);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(ApiRoutes.WS_ENDPOINT)
				.setAllowedOrigins(ConfigConstants.CORS_ALLOWED_ORIGINS.toArray(String[]::new)).withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompJwtChannelInterceptor);
	}
}
