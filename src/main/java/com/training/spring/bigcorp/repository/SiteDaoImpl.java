package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Site;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SiteDaoImpl implements SiteDao {
    @PersistenceContext
    private EntityManager em;

    private static String SELECT_WITH_JOIN ="select s from Site s";

    @Override
    public Site findById(String s){
        try {
            return em.find(Site.class, s);
        }catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Site> findAll(){
        try{
            return em.createQuery(SELECT_WITH_JOIN, Site.class).getResultList();
        }catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void delete(Site element) {
        em.remove(element);
    }

    @Override
    public void persist(Site element) {
        em.persist(element);
    }
}
