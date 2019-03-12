package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CaptorDao extends CrudDao<Captor, String> {
    List<Captor> findBySiteId(String siteId);
}
