package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.*;
import net.bytebuddy.asm.Advice;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class CaptorDaoImplTest {
    @Autowired
    private CaptorDao captorDao;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MeasureDao measureDao;

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeignKey() {
        Captor captor = captorDao.getOne("c1");
        Assertions
                .assertThatThrownBy(() -> {
                    captorDao.delete(captor);
                    entityManager.flush();
                })
                .isExactlyInstanceOf(PersistenceException.class)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void findById() {
        Optional<Captor> site = captorDao.findById("c1");
        Assertions.assertThat(site)
                .get()
                .extracting("name")
                .containsExactly("Eolienne");
    }

    @Test
    public void findBySiteId() {
        List<Captor> captors = captorDao.findBySiteId("site1");
        Assertions.assertThat(captors)
                .hasSize(2)
                .extracting("id", "name")
                .contains(Tuple.tuple("c1", "Eolienne"))
                .contains(Tuple.tuple("c2", "Laminoire à chaud"));
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Optional<Captor> captor = captorDao.findById("unknown");
        Assertions.assertThat(captor).isEmpty();
    }

    @Test
    public void findAll() {
        List<Captor> captors = captorDao.findAll();
        Assertions.assertThat(captors)
                .hasSize(2)
                .extracting("id", "name")
                .contains(Tuple.tuple("c1", "Eolienne"))
                .contains(Tuple.tuple("c2", "Laminoire à chaud"));
    }

    @Test
    public void create() {
        Assertions.assertThat(captorDao.findAll()).hasSize(2);
        Site site = new Site("Bigcorp Lyon");
        site.setId("site1");
        Captor newCaptor = new RealCaptor("New captor", site);
        newCaptor.setId("c3");
        captorDao.save(newCaptor);
        Assertions.assertThat(captorDao.findAll())
                .hasSize(3)
                .extracting(Captor::getName)
                .contains("Eolienne", "Laminoire à chaud", "New captor");
    }

    @Test
    public void update() {
        Optional<Captor> captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Eolienne");
        captor.ifPresent(c ->  {
            c.setName("Captor updated");
            captorDao.save(c);
        });
        captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Captor updated");
    }
    @Test
    public void delete() {
        Site site = new Site("Bigcorp Lyon");
        site.setId("site1");
        Captor newCaptor = new RealCaptor("New captor", site);
        newCaptor.setId("c3");
        captorDao.save(newCaptor);
        Assertions.assertThat(captorDao.findById(newCaptor.getId())).isNotEmpty();
        captorDao.delete(newCaptor);
        Assertions.assertThat(captorDao.findById(newCaptor.getId())).isEmpty();
    }

    @Test
    public void findByExample() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id")
                .withMatcher("siteId", ExampleMatcher.GenericPropertyMatchers.exact());

        Site site = new Site("Bigcorp Lyon");
        site.setId("site1");
        Captor captor = new FixedCaptor("lienn", site, null);
        List<Captor> captors = captorDao.findAll(Example.of(captor, matcher));
        Assertions.assertThat(captors)
                .hasSize(1)
                .extracting("id", "name")
                .containsExactly(Tuple.tuple("c1", "Eolienne"));
    }

    @Test
    public void preventConcurrentWrite() {
        Captor captor = captorDao.getOne("c1");
        Assertions.assertThat(captor.getVersion()).isEqualTo(0);
        entityManager.detach(captor);
        captor.setName("Captor updated");
        Captor attachedCaptor = captorDao.save(captor);
        entityManager.flush();
        Assertions.assertThat(attachedCaptor.getName()).isEqualTo("Captor updated");
        Assertions.assertThat(attachedCaptor.getVersion()).isEqualTo(1);
        Assertions.assertThatThrownBy(() -> captorDao.save(captor))
                .isExactlyInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    public void createShouldThrowExceptionWhenNameIsNull() {
        Site site = new Site("Bigcorp Lyon");
        site.setId("site1");
        Assertions
                .assertThatThrownBy(() -> {
                    captorDao.save(new RealCaptor(null, site));
                    entityManager.flush();
                })
                .isExactlyInstanceOf(javax.validation.ConstraintViolationException.class)
                .hasMessageContaining("ne peut pas être nul");
    }
    @Test
    public void createShouldThrowExceptionWhenNameSizeIsInvalid() {
        Site site = new Site("Bigcorp Lyon");
        site.setId("site1");
        Assertions
                .assertThatThrownBy(() -> {
                    captorDao.save(new RealCaptor("ee", site));
                    entityManager.flush();
                })
                .isExactlyInstanceOf(javax.validation.ConstraintViolationException.class)
                .hasMessageContaining("la taille doit être comprise entre 3 et 100");
    }

    @Test
    public void createSimulatedCaptorShouldThrowExceptionWhenMinMaxAreInvalid() {
        Site site = new Site("Bigcorp Lyon");
        site.setId("site1");
        SimulatedCaptor simulatedCaptor = new SimulatedCaptor("Mon site", site, 10, 5);
        Assertions
                .assertThatThrownBy(() -> {
                    captorDao.save(simulatedCaptor);
                    entityManager.flush();
                })
                .isExactlyInstanceOf(javax.validation.ConstraintViolationException.class)
                .hasMessageContaining("minPowerInWatt should be less than maxPowerInWatt");
    }

    @Test
    public void deleteBySiteId() {
        Assertions.assertThat(captorDao.findBySiteId("site1")).hasSize(2);
        measureDao.deleteAll();
        captorDao.deleteBySiteId("site1");
        Assertions.assertThat(captorDao.findBySiteId("site1")).isEmpty();
    }
}