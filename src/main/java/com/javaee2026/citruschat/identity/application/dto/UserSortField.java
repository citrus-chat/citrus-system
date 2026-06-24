package com.javaee2026.citruschat.identity.application.dto;

import java.util.Arrays;

public enum UserSortField {
	CREATED_AT("createdAt"), EMAIL("email"), USERNAME("username"), STATUS("status");

	private final String parameterValue;

	UserSortField(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String parameterValue() {
		return parameterValue;
	}

	public static UserSortField from(String value) {
		if (value == null || value.isBlank()) {
			return CREATED_AT;
		}

		return Arrays.stream(values()).filter(field -> field.parameterValue.equalsIgnoreCase(value)).findFirst()
				.orElse(CREATED_AT);
	}
}
