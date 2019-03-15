package com.training.spring.bigcorp.repository;


import com.training.spring.bigcorp.model.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;

    @Autowired
    private EntityManager entityManager;

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
        Captor captor = new RealCaptor("Eolienne", site);
        captor.setId("c1");
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

    @Test
    public void preventConcurrentWrite() {
        Measure measure = measureDao.getOne(-1L);
        Assertions.assertThat(measure.getVersion()).isEqualTo(0);
        entityManager.detach(measure);
        measure.setValueInWatt(2_333_666);
        Measure attachedMeasure = measureDao.save(measure);
        entityManager.flush();
        Assertions.assertThat(attachedMeasure.getValueInWatt()).isEqualTo(2_333_666);
        Assertions.assertThat(attachedMeasure.getVersion()).isEqualTo(1);
        Assertions.assertThatThrownBy(() -> measureDao.save(measure))
                .isExactlyInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    public void deleteByCaptorId() {
        Assertions.assertThat(measureDao.findAll().stream().filter(m -> m.getCaptor().getId().equals("c1"))).hasSize(5);
        measureDao.deleteByCaptorId("c1");
        Assertions.assertThat(measureDao.findAll().stream().filter(m -> m.getCaptor().getId().equals("c1"))).isEmpty();
    }

}
