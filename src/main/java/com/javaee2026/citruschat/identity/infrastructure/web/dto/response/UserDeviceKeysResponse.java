package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import java.util.List;
import java.util.UUID;

public record UserDeviceKeysResponse(UUID userId, List<DeviceResponse> devices) {
}
