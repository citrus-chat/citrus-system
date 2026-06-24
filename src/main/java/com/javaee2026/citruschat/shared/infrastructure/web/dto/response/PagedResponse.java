package com.javaee2026.citruschat.shared.infrastructure.web.dto.response;

import java.util.List;

public record PagedResponse<T>(List<T> items, PaginationMetaResponse meta) {
}
