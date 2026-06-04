package com.javaee2026.citruschat.identity.infrastructure.web.dto.request;

public record OneTimePreKeyRequest(int keyId, String publicKey) {
}
