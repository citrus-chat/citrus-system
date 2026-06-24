package com.javaee2026.citruschat.identity.application.dto;

/**
 * Query parameters accepted by the user repository for paginated user lists.
 *
 * <p>
 * Page numbers are zero-based to match Spring Data's convention.
 * </p>
 */
public record UserPageQuery(int page, int size, UserSortField sortField, SortDirection direction) {

	public static UserPageQuery of(int page, int size, String sortBy, String direction) {
		return new UserPageQuery(page, size, UserSortField.from(sortBy), SortDirection.from(direction));
	}
}
