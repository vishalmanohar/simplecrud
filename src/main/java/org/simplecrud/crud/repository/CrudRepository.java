package org.simplecrud.crud.repository;

import java.util.List;

public interface CrudRepository<T>{
    List<T> getAll(int skip, int limit);
    T findOne(String id);
    int count();
    List<T> findBy(String fieldName, String value);
    void save(T object);
    void delete(T object);
}
