package com.javaee2026.citruschat.identity.infrastructure.websocket;

import com.javaee2026.citruschat.identity.application.constants.WebLoginSocketContract;
import com.javaee2026.citruschat.identity.application.ports.IWebLoginSessionNotifier;
import com.javaee2026.citruschat.identity.application.results.WebLoginSessionResult;
import com.javaee2026.citruschat.identity.infrastructure.websocket.dto.WebLoginConfirmedEvent;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebSocketWebLoginSessionNotifier implements IWebLoginSessionNotifier {

	private static final String WEB_LOGIN_CONFIRMED_TYPE = "WEB_LOGIN_CONFIRMED";

	private final SimpMessageSendingOperations messagingTemplate;

	public WebSocketWebLoginSessionNotifier(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void notifyLoginConfirmed(String webLoginPrincipalName, WebLoginSessionResult session) {
		messagingTemplate.convertAndSendToUser(webLoginPrincipalName, WebLoginSocketContract.SERVER_USER_QUEUE,
				toEvent(session));
	}

	private WebLoginConfirmedEvent toEvent(WebLoginSessionResult session) {
		return new WebLoginConfirmedEvent(WEB_LOGIN_CONFIRMED_TYPE, session.userId().toString(), session.email(),
				session.username(), session.webDeviceId().toString(), session.accessToken(), session.tokenType(),
				session.expiresIn());
	}
}
