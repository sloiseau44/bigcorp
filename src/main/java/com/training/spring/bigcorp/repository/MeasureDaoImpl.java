package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.PowerSource;
import com.training.spring.bigcorp.model.Site;
import com.training.spring.bigcorp.utils.H2DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MeasureDaoImpl implements MeasureDao{
    private NamedParameterJdbcTemplate jdbcTemplate;
    private H2DateConverter h2DateConverter;

    public MeasureDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, H2DateConverter h2DateConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.h2DateConverter = h2DateConverter;
    }

    private static String SELECT_WITH_JOIN ="SELECT m.id, m.instant, m.value_in_watt, m.captor_id, c.name as captor_name, c.site_id, s.name as site_name " +
            "FROM Measure m inner join Captor c on m.captor_id=c.id inner join site s on c.site_id = s.id ";

    private Measure measureMapper(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site(rs.getString("site_name"));
        site.setId(rs.getString("site_id"));
        Captor captor = new Captor(rs.getString("captor_name"), PowerSource.FIXED, site);
        captor.setId(rs.getString("captor_id"));
        Measure measure = new Measure(h2DateConverter.convert(rs.getString("instant")),
                rs.getInt("value_in_watt"),
                captor);
        measure.setId(rs.getLong("id"));
        return measure;
    }

    @Override
    public List<Measure> findBySiteId(String siteId) {
        try{
            return  jdbcTemplate.query(SELECT_WITH_JOIN+"WHERE c.site_id = :id", new MapSqlParameterSource().addValue("id", siteId), this::measureMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Measure> findByCaptorId(String captorId) {
        try{
            return  jdbcTemplate.query(SELECT_WITH_JOIN+"WHERE m.captor_id = :id", new MapSqlParameterSource().addValue("id", captorId), this::measureMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void create(Measure element) {
        jdbcTemplate.update("INSERT INTO MEASURE (INSTANT, VALUE_IN_WATT, CAPTOR_ID) VALUES(:instant, :valueInWatt, :captor_id);", new MapSqlParameterSource()
                .addValue("instant", element.getInstant())
                .addValue("valueInWatt", element.getValueInWatt())
                .addValue("captor_id", element.getCaptor().getId()));
    }

    @Override
    public Measure findById(String s) {
        try{
            return jdbcTemplate.queryForObject(SELECT_WITH_JOIN+" WHERE m.id=:id", new MapSqlParameterSource("id", s), this::measureMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<Measure> findAll() {
        try{
            return jdbcTemplate.query(SELECT_WITH_JOIN, this::measureMapper);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void update(Measure element) {
        jdbcTemplate.update("UPDATE MEASURE SET INSTANT = :instant, VALUE_IN_WATT = :valueInWatt, CAPTOR_ID = :captor_id where id =:id", new MapSqlParameterSource()
                .addValue("id", element.getId())
                .addValue("instant", element.getInstant())
                .addValue("valueInWatt", element.getValueInWatt())
                .addValue("captor_id", element.getCaptor().getId()));
    }

    @Override
    public void deleteById(String s) {
        jdbcTemplate.update("DELETE FROM MEASURE WHERE id=:id", new MapSqlParameterSource().addValue("id", s));
    }
}
