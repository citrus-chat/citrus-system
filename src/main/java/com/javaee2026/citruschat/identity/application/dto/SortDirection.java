package com.javaee2026.citruschat.identity.application.dto;

public enum SortDirection {
	ASC, DESC;

	public static SortDirection from(String value) {
		return "asc".equalsIgnoreCase(value) ? ASC : DESC;
	}
}
