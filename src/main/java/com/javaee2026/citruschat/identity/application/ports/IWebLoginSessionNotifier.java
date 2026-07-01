package com.javaee2026.citruschat.identity.application.ports;

import com.javaee2026.citruschat.identity.application.results.WebLoginSessionResult;

public interface IWebLoginSessionNotifier {

	void notifyLoginConfirmed(String webLoginPrincipalName, WebLoginSessionResult session);
}
