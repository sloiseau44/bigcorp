package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.PowerSource;
import com.training.spring.bigcorp.model.Site;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class SiteDaoImpl implements SiteDao {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public SiteDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Site captorMapper(ResultSet rs, int rowNum) throws SQLException {
        Captor captor = new Captor(rs.getString("captor_name"));
        captor.setId(rs.getString("captor_id"));
        Site site = new Site(rs.getString("name"), PowerSource.FIXED, site);
        site.setId(rs.getString("id"));
        return site;
    }

    @Override
    public void create(Site element) {

    }

    @Override
    public Site findById(String s) {
        return null;
    }

    @Override
    public List<Site> findAll() {
        return null;
    }

    @Override
    public void update(Site element) {

    }

    @Override
    public void deleteById(String s) {

    }
}
