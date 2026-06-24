package com.javaee2026.citruschat.identity.infrastructure.web.controller;

import com.javaee2026.citruschat.identity.application.dto.SortDirection;
import com.javaee2026.citruschat.identity.application.dto.UserPageQuery;
import com.javaee2026.citruschat.identity.application.dto.UserSortField;
import com.javaee2026.citruschat.identity.application.results.UserResult;
import com.javaee2026.citruschat.identity.application.usecases.GetAdminUsersUseCase;
import com.javaee2026.citruschat.shared.application.results.PagedResult;
import com.javaee2026.citruschat.shared.domain.constants.ApiResponseMessages;
import com.javaee2026.citruschat.shared.infrastructure.constants.ApiRoutes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class GetAdminUsersControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GetAdminUsersUseCase getAdminUsersUseCase;

	@Test
	@WithMockUser
	void shouldReturnTheDefaultPageInsideTheApiResponseEnvelope() throws Exception {
		when(getAdminUsersUseCase.execute(any()))
				.thenReturn(new PagedResult<>(List.of(user(1)), 1, 0, 20, 0, false, false));

		mockMvc.perform(get(ApiRoutes.API_ADMIN_USERS)).andExpect(status().isOk())
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message", is(ApiResponseMessages.ADMIN_USERS_RETRIEVED_SUCCESS)))
				.andExpect(jsonPath("$.data.items[0].email", is("user1@citruschat.com")))
				.andExpect(jsonPath("$.data.meta.currentPage", is(0)))
				.andExpect(jsonPath("$.data.meta.perPage", is(20))).andExpect(jsonPath("$.data.meta.total", is(1)))
				.andExpect(jsonPath("$.data.meta.lastPage", is(0))).andExpect(jsonPath("$.data.meta.from", is(1)))
				.andExpect(jsonPath("$.data.meta.to", is(1))).andExpect(jsonPath("$.data.meta.hasNextPage", is(false)))
				.andExpect(jsonPath("$.data.meta.hasPreviousPage", is(false)));

		UserPageQuery query = captureQuery();
		assertEquals(0, query.page());
		assertEquals(20, query.size());
		assertEquals(UserSortField.CREATED_AT, query.sortField());
		assertEquals(SortDirection.DESC, query.direction());
	}

	@Test
	@WithMockUser
	void shouldReturnMetadataForTheRequestedPageAndSort() throws Exception {
		when(getAdminUsersUseCase.execute(any())).thenReturn(new PagedResult<>(
				IntStream.rangeClosed(1, 10).mapToObj(this::user).toList(), 135, 1, 10, 13, true, true));

		mockMvc.perform(get(ApiRoutes.API_ADMIN_USERS).param("page", "1").param("size", "10").param("sortBy", "email")
				.param("direction", "asc")).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.items.length()", is(10)))
				.andExpect(jsonPath("$.data.meta.currentPage", is(1)))
				.andExpect(jsonPath("$.data.meta.perPage", is(10))).andExpect(jsonPath("$.data.meta.total", is(135)))
				.andExpect(jsonPath("$.data.meta.lastPage", is(13))).andExpect(jsonPath("$.data.meta.from", is(11)))
				.andExpect(jsonPath("$.data.meta.to", is(20))).andExpect(jsonPath("$.data.meta.hasNextPage", is(true)))
				.andExpect(jsonPath("$.data.meta.hasPreviousPage", is(true)));

		UserPageQuery query = captureQuery();
		assertEquals(1, query.page());
		assertEquals(10, query.size());
		assertEquals(UserSortField.EMAIL, query.sortField());
		assertEquals(SortDirection.ASC, query.direction());
	}

	@Test
	@WithMockUser
	void shouldNormalizeNegativePagesAndInvalidPageSizes() throws Exception {
		when(getAdminUsersUseCase.execute(any())).thenReturn(new PagedResult<>(List.of(), 0, 0, 20, 0, false, false));

		mockMvc.perform(get(ApiRoutes.API_ADMIN_USERS).param("page", "-1").param("size", "0"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.data.meta.currentPage", is(0)))
				.andExpect(jsonPath("$.data.meta.perPage", is(20))).andExpect(jsonPath("$.data.meta.from", is(0)))
				.andExpect(jsonPath("$.data.meta.to", is(0)));

		UserPageQuery query = captureQuery();
		assertEquals(0, query.page());
		assertEquals(20, query.size());
	}

	@Test
	@WithMockUser
	void shouldLimitPageSizeAndFallBackToTheDefaultSort() throws Exception {
		when(getAdminUsersUseCase.execute(any())).thenReturn(new PagedResult<>(List.of(), 0, 0, 100, 0, false, false));

		mockMvc.perform(get(ApiRoutes.API_ADMIN_USERS).param("size", "9999").param("sortBy", "passwordHash")
				.param("direction", "sideways")).andExpect(status().isOk());

		UserPageQuery query = captureQuery();
		assertEquals(100, query.size());
		assertEquals(UserSortField.CREATED_AT, query.sortField());
		assertEquals(SortDirection.DESC, query.direction());
	}

	private UserPageQuery captureQuery() {
		ArgumentCaptor<UserPageQuery> captor = ArgumentCaptor.forClass(UserPageQuery.class);
		verify(getAdminUsersUseCase).execute(captor.capture());
		return captor.getValue();
	}

	private UserResult user(int index) {
		return new UserResult(new UUID(0, index), "user" + index, "user" + index + "@citruschat.com", true, null);
	}
}
