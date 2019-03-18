package com.training.spring.bigcorp.service;

import com.training.spring.bigcorp.config.Monitored;
import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.FixedCaptor;
import com.training.spring.bigcorp.model.RealCaptor;
import com.training.spring.bigcorp.model.SimulatedCaptor;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.service.measure.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CaptorServiceImpl implements CaptorService{

    private MeasureService<FixedCaptor> fixedMeasureService;
    private MeasureService<RealCaptor> realMeasureService;
    private MeasureService<SimulatedCaptor> simulatedMeasureService;

    @Autowired
    private CaptorDao captorDao;

    public CaptorServiceImpl(){};

    public CaptorServiceImpl (MeasureService fixedMeasureService, MeasureService realMeasureService, MeasureService simulatedMeasureService){
        this.fixedMeasureService=fixedMeasureService;
        this.realMeasureService=realMeasureService;
        this.simulatedMeasureService=simulatedMeasureService;
    }

    @Override
    @Monitored
    public Set<Captor> findBySite(String siteId) {
        Set<Captor> listCaptor = captorDao.findBySiteId(siteId).stream().collect(Collectors.toSet());


       /* Set<Captor> captors = new HashSet<>();
        if (siteId == null) {
            return captors;
        }
        captors.add(new Captor("Capteur fixed", PowerSource.FIXED, new Site("bigcorp")));
       /* captors.add(new Captor("Capteur real", PowerSource.REAL));
        captors.add(new Captor("Capteur simulated", PowerSource.SIMULATED));*/
        return listCaptor;
    }
}
