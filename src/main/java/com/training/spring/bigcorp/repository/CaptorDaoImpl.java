package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Repository
public class CaptorDaoImpl implements CaptorDao {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MeasureDao measureDao;

    private static String SELECT_WITH_JOIN ="select c from Captor c inner join c.site s ";

    @Override
    public List<Captor> findBySiteId(String siteId) {
        try{
            return em.createQuery(SELECT_WITH_JOIN+"where s.id = :siteId", Captor.class).setParameter("siteId", siteId).getResultList();
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Captor findById(String s) {
        try{
            return em.find(Captor.class, s);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Captor> findAll() {
        try{
            return em.createQuery(SELECT_WITH_JOIN, Captor.class).getResultList();
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void delete(Captor element) {
        measureDao.findByCaptorId(element.getId()).forEach(m -> measureDao.delete(m));
        em.remove(element);


    }

    @Override
    public void persist(Captor element) {
        em.persist(element);
    }


}
