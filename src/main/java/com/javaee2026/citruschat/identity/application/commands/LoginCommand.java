package com.javaee2026.citruschat.identity.application.commands;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.OneTimePreKeyRequest;
import com.javaee2026.citruschat.identity.infrastructure.web.dto.request.SignedPreKeyRequest;

import java.util.List;
import java.util.UUID;

public record LoginCommand(String email, String password, UUID deviceId, String deviceName, DeviceType deviceType,
		String publicIdentityKey, SignedPreKeyRequest signedPreKey, List<OneTimePreKeyRequest> oneTimePreKeys) {
}
