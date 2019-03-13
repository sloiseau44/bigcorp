package com.training.spring.bigcorp.repository;


import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.PowerSource;
import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

@RunWith(SpringRunner.class)
@JdbcTest
@ContextConfiguration(classes = {DaoTestConfig.class})
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;

    private Site site;
    private Captor captor;



    @Before
    public void init(){
        site = new Site("name");
        site.setId("site1");
        captor = new Captor("Eolienne", PowerSource.FIXED, site);
        captor.setId("c1");
    }

    @Test
    public void findById() {
        Measure measure = measureDao.findById("1");
        Assertions.assertThat(measure.getInstant()).isEqualTo(Instant.parse("2018-08-09T11:00:00.000Z"));
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Measure measure = measureDao.findById("0");
        Assertions.assertThat(measure).isNull();
    }

    @Test
    public void findAll() {
        List<Measure> measures = measureDao.findAll();
        Assertions.assertThat(measures).hasSize(10);
    }

    @Test
    public void findBySiteId() {
        List<Measure> measures = measureDao.findBySiteId("site1");
        Assertions.assertThat(measures).hasSize(10);
    }

    @Test
    public void findByCaptorId() {
        List<Measure> measures = measureDao.findByCaptorId("c1");
        Assertions.assertThat(measures).hasSize(5);
    }

    @Test
    public void create() {
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.create(new Measure(Instant.now(), 2_333_666, captor));
        Assertions.assertThat(measureDao.findAll())
                .hasSize(11);
    }

    @Test
    public void update() {
        Measure measure = measureDao.findById("1");
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(1000000);
        measure.setValueInWatt(2000000);
        measureDao.update(measure);
        measure = measureDao.findById("1");
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(2000000);
    }

    @Test
    public void deleteById() {
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.deleteById("1");
        Assertions.assertThat(measureDao.findAll()).hasSize(9);
    }






}
