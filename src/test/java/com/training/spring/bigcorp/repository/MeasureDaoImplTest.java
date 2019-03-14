package com.training.spring.bigcorp.repository;


import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.PowerSource;
import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;

    @Test
    public void findById() {
        Optional<Measure> measure = measureDao.findById(-1L);
        Assertions.assertThat(measure)
                .get()
                .extracting("instant")
                .containsExactly(Instant.parse("2018-08-09T11:00:00.000Z"));
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Optional<Measure> measure = measureDao.findById(0L);
        Assertions.assertThat(measure).isEmpty();
    }

    @Test
    public void findAll() {
        List<Measure> measures = measureDao.findAll();
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
        Site site = new Site("Bigcorp Lyon");
        site.setId("site1");
        Captor captor = new Captor("Eolienne", site);
        captor.setId("c1");
        captor.setPowerSource(PowerSource.SIMULATED);
        measureDao.save(new Measure(Instant.now(), 2_333_666, captor));
        Assertions.assertThat(measureDao.findAll()).hasSize(11);
    }

    @Test
    public void update() {
        Optional<Measure> measure = measureDao.findById(-1L);
        Assertions.assertThat(measure).get().extracting("valueInWatt").containsExactly(1_000_000);
        measure.ifPresent(m ->  {
            m.setValueInWatt(2_333_666);
            measureDao.save(m);
        });
        measure = measureDao.findById(-1L);
        Assertions.assertThat(measure).get().extracting("valueInWatt").containsExactly(2_333_666);
    }

    @Test
    public void deleteById() {
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.delete(measureDao.findById(-1L).get());
        Assertions.assertThat(measureDao.findAll()).hasSize(9);
    }





}
