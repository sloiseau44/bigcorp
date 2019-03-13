package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.PowerSource;
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
public class CaptorDaoImpl implements CaptorDao {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public CaptorDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String SELECT_WITH_JOIN ="SELECT c.id, c.name, c.site_id, s.name as site_name FROM Captor c inner join Site s on c.site_id = s.id ";

    private Captor captorMapper(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site(rs.getString("site_name"));
        site.setId(rs.getString("site_id"));
        Captor captor = new Captor(rs.getString("name"), PowerSource.FIXED, site);
        captor.setId(rs.getString("id"));
        return captor;
    }

    @Override
    public List<Captor> findBySiteId(String siteId) {
        try{
            return  jdbcTemplate.query(SELECT_WITH_JOIN+"WHERE c.site_id=:id", new MapSqlParameterSource().addValue("id", siteId), this::captorMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void create(Captor element) {
        jdbcTemplate.update("INSERT INTO CAPTOR (ID, NAME, site_id) VALUES(:id, :name, :site_id)", new MapSqlParameterSource()
                .addValue("id", element.getId())
                .addValue("name", element.getName())
                .addValue("site_id", element.getSite().getId()));
    }

    @Override
    public Captor findById(String s) {
        try{
            return jdbcTemplate.queryForObject(SELECT_WITH_JOIN+" WHERE c.id=:id", new MapSqlParameterSource("id", s), this::captorMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Captor> findAll() {
        try{
            return jdbcTemplate.query(SELECT_WITH_JOIN, this::captorMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void update(Captor element) {
        jdbcTemplate.update("UPDATE Captor SET name = :name, site_id = :site_id where id =:id", new MapSqlParameterSource()
                .addValue("id", element.getId())
                .addValue("name", element.getName())
                .addValue("site_id", element.getSite().getId()));
    }


    @Override
    public void deleteById(String s) {
        jdbcTemplate.update("DELETE FROM CAPTOR WHERE id=:id", new MapSqlParameterSource().addValue("id", s));
    }
}
