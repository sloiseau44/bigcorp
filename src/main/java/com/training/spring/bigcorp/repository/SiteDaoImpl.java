package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Site;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

    private Site siteMapper(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site(rs.getString("name"));
        site.setId(rs.getString("id"));
        return site;
    }

    @Override
    public void create(Site element) {
        jdbcTemplate.update("INSERT INTO SITE (ID, NAME) VALUES(:id, :name)", new MapSqlParameterSource()
                .addValue("id", element.getId())
                .addValue("name", element.getName()));
    }

    @Override
    public Site findById(String s){
        try {
            return jdbcTemplate.queryForObject("SELECT id, name FROM SITE WHERE id=:id", new MapSqlParameterSource("id", s), this::siteMapper);
        }catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Site> findAll(){
        try{
            return jdbcTemplate.query("SELECT id, name FROM SITE", this::siteMapper);
        }catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Site element) {
            jdbcTemplate.update("UPDATE Site SET name = :name where id =:id", new MapSqlParameterSource()
                    .addValue("id", element.getId())
                    .addValue("name", element.getName()));
    }

    @Override
    public void deleteById(String s) {
        jdbcTemplate.update("DELETE FROM SITE WHERE id=:id", new MapSqlParameterSource().addValue("id", s));
    }
}
