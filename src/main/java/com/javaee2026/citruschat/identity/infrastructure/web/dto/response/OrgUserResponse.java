package com.javaee2026.citruschat.identity.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Datos de un usuario para construir el organigrama en el frontend. Se
 * devuelven todos los usuarios activos con su posición y relación jerárquica.
 */
public record OrgUserResponse(

		@JsonProperty("id") UUID id, @JsonProperty("username") String username,
		@JsonProperty("avatar_url") String avatarUrl, @JsonProperty("position") String position,
		@JsonProperty("hierarchy_level") Integer hierarchyLevel, @JsonProperty("manager_id") UUID managerId

) {
}
