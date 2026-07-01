package com.javaee2026.citruschat.messaging.infrastructure.websocket.configuration;

import com.javaee2026.citruschat.identity.application.constants.WebLoginSocketContract;
import com.javaee2026.citruschat.identity.application.ports.IWebLoginTokenRepository;
import com.javaee2026.citruschat.identity.application.security.WebLoginPrincipalName;
import com.javaee2026.citruschat.identity.application.security.WebLoginTokenSecurity;
import com.javaee2026.citruschat.identity.domain.model.WebLoginToken;

import java.time.Instant;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class StompJwtChannelInterceptor implements ChannelInterceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private final JwtDecoder jwtDecoder;
	private final IWebLoginTokenRepository webLoginTokenRepository;
	private final WebLoginTokenSecurity webLoginTokenSecurity;

	public StompJwtChannelInterceptor(JwtDecoder jwtDecoder, IWebLoginTokenRepository webLoginTokenRepository,
			WebLoginTokenSecurity webLoginTokenSecurity) {
		this.jwtDecoder = jwtDecoder;
		this.webLoginTokenRepository = webLoginTokenRepository;
		this.webLoginTokenSecurity = webLoginTokenSecurity;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor == null || accessor.getCommand() == null) {
			return message;
		}

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authHeader = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				authenticateJwt(accessor, authHeader);
				return message;
			}

			String webLoginToken = accessor.getFirstNativeHeader(WebLoginSocketContract.TOKEN_HEADER);

			if (webLoginToken != null && !webLoginToken.isBlank()) {
				authenticatePendingWebLogin(accessor, webLoginToken);
				return message;
			}

			throw new IllegalArgumentException(
					"Missing Authorization or " + WebLoginSocketContract.TOKEN_HEADER + " header");
		}

		return message;
	}

	private void authenticateJwt(StompHeaderAccessor accessor, String authHeader) {
		String token = authHeader.substring("Bearer ".length());
		var jwt = jwtDecoder.decode(token);

		Authentication authentication = new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, List.of());

		accessor.setUser(authentication);
	}

	private void authenticatePendingWebLogin(StompHeaderAccessor accessor, String token) {
		String tokenHash = webLoginTokenSecurity.hashToken(token);
		WebLoginToken webLoginToken = webLoginTokenRepository.findByTokenHash(tokenHash)
				.orElseThrow(() -> new IllegalArgumentException("Invalid web login token"));

		if (webLoginToken.isUsed()) {
			throw new IllegalArgumentException("Web login token already used");
		}

		if (webLoginToken.isExpired(Instant.now())) {
			throw new IllegalArgumentException("Web login token expired");
		}

		String principalName = WebLoginPrincipalName.fromDeviceId(webLoginToken.getWebDeviceId());
		Authentication authentication = new UsernamePasswordAuthenticationToken(principalName, null, List.of());

		accessor.setUser(authentication);
	}
}
