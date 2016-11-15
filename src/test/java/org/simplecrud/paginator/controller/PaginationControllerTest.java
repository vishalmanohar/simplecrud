package org.simplecrud.paginator.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.simplecrud.paginator.Paging;
import org.simplecrud.paginator.contract.FilterParams;
import org.simplecrud.paginator.contract.SortParams;
import org.simplecrud.paginator.repository.AllPagingServices;
import org.simplecrud.paginator.response.PageResults;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PaginationControllerTest {

    @Mock
    AllPagingServices allPagingServices;
    PaginationController paginationController;

    @Before
    public void setup() {
        initMocks(this);
        paginationController = new PaginationController(allPagingServices);
    }

    @Test
    public void shouldPageEntity() throws Exception {
        PageResults<String> results = new PageResults<>();
        results.setResults(asList("someString"));
        results.setPageNo(0);
        results.setTotalRows(1);

        Paging pagingService = mock(Paging.class);
        when(allPagingServices.getPagingServiceFor("entity1")).thenReturn(pagingService);
        when(pagingService.page(any(Integer.class), any(Integer.class), any(FilterParams.class), any(SortParams.class))).thenReturn(results);

        standaloneSetup(paginationController).build()
                .perform(get("/page/entity1").param("pageNo", "1").param("rowsPerPage", "2").param("searchCriteria", "{\"name\":\"goodName\"}")
                        .param("sortCriteria", "{\"name\":\"asc\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"pageNo\":0,\"totalRows\":1,\"results\":[\"someString\"]}"));

        ArgumentCaptor<FilterParams> searchCriteriaCaptor = ArgumentCaptor.forClass(FilterParams.class);
        ArgumentCaptor<SortParams> sortCriteriaCaptor = ArgumentCaptor.forClass(SortParams.class);
        verify(pagingService).page(eq(1), eq(2), searchCriteriaCaptor.capture(), sortCriteriaCaptor.capture());
        assertEquals("goodName", searchCriteriaCaptor.getValue().get("name"));
        assertEquals("asc", sortCriteriaCaptor.getValue().get("name"));
    }

}
