package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Measure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasureDao extends JpaRepository<Measure, Long> {
    List<Measure> findByCaptorId(String captorId);
    void deleteByCaptorId(String captorId);
}
