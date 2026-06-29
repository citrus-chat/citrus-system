package com.javaee2026.citruschat.identity.application.results;

import java.util.UUID;

/**
 * Resultado del perfil de un usuario visto por un tercero. Los campos sensibles
 * pueden ser null cuando el usuario los ocultó con sus ajustes de privacidad y
 * el solicitante no es su jefe.
 */
public record PublicUserProfileResult(

		UUID userId, String username, String avatarUrl,

		// Datos personales (pueden ser nulos si el usuario los desactivo)
		String phoneNumber, String email, String description, String status,

		// Datos de la organización (siempre visibles)
		String positionName, String department, Integer hierarchyLevel, UUID managerId, String managerUsername,

		// Banderas de visibilidad que el frontend puede usar para mostrar como
		// candaditos
		boolean showPhone, boolean showEmail, boolean showStatus, boolean showDescription

) {
}
