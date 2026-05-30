package com.javaee2026.citruschat.messaging.infrastructure.websocket.configuration;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Interceptor que valida JWT en la fase de handshake WebSocket. Extrae el token
 * del header Authorization y lo valida.
 */
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

	private final JwtDecoder jwtDecoder;

	public JwtHandshakeInterceptor(JwtDecoder jwtDecoder) {
		this.jwtDecoder = jwtDecoder;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		try {
			String authHeader = request.getHeaders().getFirst("Authorization");

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return false;
			}

			String token = authHeader.substring("Bearer ".length());
			var jwt = jwtDecoder.decode(token);

			// Guardar en atributos de la sesión WebSocket para acceso posterior
			attributes.put("jwt", jwt);
			attributes.put("userId", jwt.getSubject());

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}
}
