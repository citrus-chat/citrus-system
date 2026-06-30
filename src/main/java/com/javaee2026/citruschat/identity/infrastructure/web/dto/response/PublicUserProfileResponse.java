package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Respuesta del perfil público de un usuario visto por un tercero. Los campos
 * sensibles pueden ser null cuando el usuario los ocultó con sus ajustes de
 * privacidad.
 */
public record PublicUserProfileResponse(

		@JsonProperty("user_id") UUID userId, @JsonProperty("username") String username,
		@JsonProperty("avatar_url") String avatarUrl,

		// Datos personales (null si el usuario los ocultó)
		@JsonProperty("phone_number") String phoneNumber, @JsonProperty("email") String email,
		@JsonProperty("description") String description, @JsonProperty("status") String status,

		// Datos de organización (siempre visibles)
		@JsonProperty("position_name") String positionName, @JsonProperty("department") String department,
		@JsonProperty("hierarchy_level") Integer hierarchyLevel, @JsonProperty("manager_id") UUID managerId,
		@JsonProperty("manager_username") String managerUsername,

		// Banderas de visibilidad (para que el frontend muestre candados)
		@JsonProperty("show_phone") boolean showPhone, @JsonProperty("show_email") boolean showEmail,
		@JsonProperty("show_status") boolean showStatus, @JsonProperty("show_description") boolean showDescription

) {
}
