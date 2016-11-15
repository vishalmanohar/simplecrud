package org.simplecrud.paginator.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResults<E> {

    private Integer pageNo;
    private Integer totalRows;
    private List<E> results;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public List<E> getResults() {
        return results;
    }

    public void setResults(List<E> results) {
        this.results = results;
    }
}
