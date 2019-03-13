package com.training.spring.bigcorp.repository;

import java.util.List;

public interface CrudDao <T , ID> {
    T findById(ID id);
    List<T> findAll();
    void delete(T element);
    void persist(T element);
}