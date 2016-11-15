package org.simplecrud.crud.service;

import org.simplecrud.crud.model.CrudModel;
import org.simplecrud.crud.repository.CrudRepository;
import org.simplecrud.paginator.contract.FilterParams;
import org.simplecrud.paginator.contract.SortParams;
import org.simplecrud.paginator.response.PageResults;
import org.simplecrud.paginator.Paging;

public interface CrudEntity<T> extends Paging {

    /**
     * A repository to perform CRUD on
     * @return
     */
    CrudRepository getRepository();

    /**
     * A model to render the UI
     * @return
     */
    CrudModel getModel();

    /**
     * Type of the entity
     * @return
     */
    Class getEntityType();

    /**
     * Paging
     * @param pageNumber
     * @param rowsPerPage
     * @param filterParams
     * @param sortCriteria
     * @return
     */
    PageResults page(Integer pageNumber, Integer rowsPerPage, FilterParams filterParams, SortParams sortCriteria);

    /**
     * Post delete handler
     * @param object
     */
    void deleted(T object);

    /**
     * Post update handler
     * @param object
     */
    void updated(T object);
}
