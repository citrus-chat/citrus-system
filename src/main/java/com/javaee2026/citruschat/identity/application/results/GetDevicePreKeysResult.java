package com.javaee2026.citruschat.identity.application.results;

import java.util.UUID;

public record GetDevicePreKeysResult(UUID deviceId,

		String identityKey,

		int signedPreKeyId, String signedPreKey, String signedPreKeySignature,

		Integer oneTimePreKeyId, String oneTimePreKey) {
}
