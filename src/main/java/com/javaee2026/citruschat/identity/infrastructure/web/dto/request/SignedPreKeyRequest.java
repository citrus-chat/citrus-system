package com.javaee2026.citruschat.identity.infrastructure.web.dto.request;

public record SignedPreKeyRequest(int keyId, String publicKey, String signature) {
}
