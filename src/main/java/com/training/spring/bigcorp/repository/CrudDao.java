package com.training.spring.bigcorp.repository;

import java.util.List;

public interface CrudDao <T , ID> {
    void create(T element);
    T findById(ID id);
    List<T> findAll();
    void update(T element);
    void deleteById(ID id);
}