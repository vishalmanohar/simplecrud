package org.simplecrud.crud.repository;

import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@EqualsAndHashCode
public class PageRequest implements Pageable {
    private int skip;
    private int limit;

    public PageRequest(int skip, int limit) {
        this.skip = skip;
        this.limit = limit;
    }


    @Override
    public int getPageNumber() {
        return (skip/limit) + 1;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public int getOffset() {
        return skip;
    }

    @Override
    public Sort getSort() {
        return null;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}