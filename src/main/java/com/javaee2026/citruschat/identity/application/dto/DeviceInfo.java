package com.javaee2026.citruschat.identity.application.dto;

import com.javaee2026.citruschat.identity.domain.enums.DeviceType;
import com.javaee2026.citruschat.identity.domain.valueobjects.PublicKey;

import java.util.UUID;

public record DeviceInfo(UUID deviceId, String deviceName, DeviceType deviceType, PublicKey publicKey) {
}
