package com.javaee2026.citruschat.shared.domain.constants;

import java.util.List;

public final class ConfigConstants {
	private ConfigConstants() {
	}

	public static final List<String> CORS_ALLOWED_ORIGINS = List.of("http://localhost:5174", "http://localhost:8200",
			"http://localhost:63342");
}
