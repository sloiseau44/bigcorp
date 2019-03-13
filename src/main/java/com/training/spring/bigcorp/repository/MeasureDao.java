package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Measure;

import java.util.List;

public interface MeasureDao extends CrudDao<Measure, String> {
    List<Measure> findBySiteId(String siteId);
    List<Measure> findByCaptorId(String captorId);
}
