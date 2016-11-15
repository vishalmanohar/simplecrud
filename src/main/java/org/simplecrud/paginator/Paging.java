package org.simplecrud.paginator;

import org.simplecrud.paginator.contract.FilterParams;
import org.simplecrud.paginator.contract.SortParams;
import org.simplecrud.paginator.response.PageResults;

public interface Paging<E> {

    PageResults<E> page(Integer pageNo, Integer rowsPerPage, FilterParams filterCriteria, SortParams sortCriteria);

    String entityName();

}
