package org.simplecrud.crud.event;

public interface CrudEventHandler<T> {
    void deleted(T object);
    void updated(T object);
}
