package com.javaee2026.citruschat.shared.infrastructure.web.dto.response;

import com.javaee2026.citruschat.shared.application.results.PagedResult;

public record PaginationMetaResponse(int currentPage, int perPage, long total, int lastPage, int from, int to,
		boolean hasNextPage, boolean hasPreviousPage) {

	public static PaginationMetaResponse from(PagedResult<?> result) {
		if (result.total() == 0 || result.items().isEmpty()) {
			return new PaginationMetaResponse(result.currentPage(), result.perPage(), result.total(), result.lastPage(),
					0, 0, result.hasNextPage(), result.hasPreviousPage());
		}

		int from = result.currentPage() * result.perPage() + 1;
		int to = from + result.items().size() - 1;

		return new PaginationMetaResponse(result.currentPage(), result.perPage(), result.total(), result.lastPage(),
				from, to, result.hasNextPage(), result.hasPreviousPage());
	}
}
