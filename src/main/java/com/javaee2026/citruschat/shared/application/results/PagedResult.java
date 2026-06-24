package com.javaee2026.citruschat.shared.application.results;

import java.util.List;

/**
 * Framework-independent page result. Page indexes are zero-based.
 */
public record PagedResult<T>(List<T> items, long total, int currentPage, int perPage, int lastPage, boolean hasNextPage,
		boolean hasPreviousPage) {
}
