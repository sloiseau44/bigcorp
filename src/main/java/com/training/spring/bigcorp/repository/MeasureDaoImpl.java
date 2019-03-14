package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Measure;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class MeasureDaoImpl implements MeasureDao{
    @PersistenceContext
    private EntityManager em;

    private static String SELECT_WITH_JOIN ="select m from Measure m inner join m.captor c inner join c.site s";

    @Override
    public List<Measure> findBySiteId(String siteId) {
        try{
            return em.createQuery(SELECT_WITH_JOIN+" where s.id = :siteId", Measure.class).setParameter("siteId", siteId).getResultList();
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Measure> findByCaptorId(String captorId) {
        try{
            return em.createQuery(SELECT_WITH_JOIN+" where c.id = :captorId", Measure.class).setParameter("captorId", captorId).getResultList();
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Measure findById(Long s) {
        try{
            return em.find(Measure.class, s);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }


    @Override
    public List<Measure> findAll() {
        try{
            return em.createQuery(SELECT_WITH_JOIN, Measure.class).getResultList();
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void delete(Measure element) {
        em.remove(element);
    }

    @Override
    public void persist(Measure element) {
        em.persist(element);
    }
}
