package org.simplecrud.paginator.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.simplecrud.paginator.contract.FilterParams;
import org.simplecrud.paginator.contract.SortParams;
import org.simplecrud.paginator.repository.AllPagingServices;
import org.simplecrud.paginator.response.PageResults;
import org.simplecrud.paginator.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping(value = "/page")
public class PaginationController {

    private AllPagingServices allPagingServices;

    private static final Logger logger = LoggerFactory.getLogger(PaginationController.class);

    @Autowired
    public PaginationController(AllPagingServices allPagingServices) {
        this.allPagingServices = allPagingServices;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{entity}")
    @ResponseBody
    public PageResults page(@PathVariable String entity,
                            @RequestParam(value = "pageNo", defaultValue = "1") String pageNo,
                            @RequestParam("rowsPerPage") String rowsPerPage,
                            @RequestParam(value = "searchCriteria", required = false, defaultValue = "{}") String searchCriteria,
                            @RequestParam(value = "sortCriteria", required = false, defaultValue = "{}") String sortCriteria) throws IOException {
        Paging pagingService = getPagingService(entity);
        return fetchData(pageNo, rowsPerPage, createCriteria(searchCriteria), createSortCriteria(sortCriteria), pagingService);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(RuntimeException ex) {
        logger.error("Error occurred", ex);
        throw ex;
    }

    private PageResults fetchData(String pageNo, String rowsPerPage, FilterParams searchCriteria, SortParams sortCriteria, Paging pagingService) {
        return (pagingService != null) ? pagingService.page(parseInt(pageNo), parseInt(rowsPerPage), searchCriteria, sortCriteria) : null;
    }

    private FilterParams createCriteria(String searchCriteria) throws IOException {
        return new ObjectMapper().readValue(searchCriteria, FilterParams.class);
    }

    private SortParams createSortCriteria(String sortCriteria) throws IOException {
        return new ObjectMapper().readValue(sortCriteria, SortParams.class);
    }

    private Paging getPagingService(String entity) {
        Paging pagingService = allPagingServices.getPagingServiceFor(entity);
        if (pagingService != null) {
            return pagingService;
        } else {
            throw new RuntimeException("No service found which provides paging for " + entity);
        }
    }
}
