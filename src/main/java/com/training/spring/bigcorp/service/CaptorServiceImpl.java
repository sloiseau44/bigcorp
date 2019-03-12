package com.training.spring.bigcorp.service;

import com.training.spring.bigcorp.config.Monitored;
import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.PowerSource;
import com.training.spring.bigcorp.service.measure.MeasureService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CaptorServiceImpl implements CaptorService{

    private MeasureService fixedMeasureService;
    private MeasureService realMeasureService;
    private MeasureService simulatedMeasureService;

    public CaptorServiceImpl(){};

    public CaptorServiceImpl (MeasureService fixedMeasureService, MeasureService realMeasureService, MeasureService simulatedMeasureService){
        this.fixedMeasureService=fixedMeasureService;
        this.realMeasureService=realMeasureService;
        this.simulatedMeasureService=simulatedMeasureService;
    }

    @Override
    @Monitored
    public Set<Captor> findBySite(String siteId) {
        Set<Captor> captors = new HashSet<>();
        if (siteId == null) {
            return captors;
        }
        captors.add(new Captor("Capteur fixed", PowerSource.FIXED));
       /* captors.add(new Captor("Capteur real", PowerSource.REAL));
        captors.add(new Captor("Capteur simulated", PowerSource.SIMULATED));*/
        return captors;
    }
}
