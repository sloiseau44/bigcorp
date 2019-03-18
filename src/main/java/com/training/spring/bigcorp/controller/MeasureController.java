package com.training.spring.bigcorp.controller;

import com.training.spring.bigcorp.controller.dto.MeasureDto;
import com.training.spring.bigcorp.model.*;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.service.measure.SimulatedMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MeasureController {
    @Autowired
    private CaptorDao captorDao;

    @Autowired
    private SimulatedMeasureService simulatedMeasureService;

    @GetMapping("/measures/captors/{id}/last/hours/{nbHours}")
    public List<MeasureDto> findAll(@PathVariable String id, @PathVariable String nbHours) {
        Captor captor = captorDao.findById(id).orElseThrow(IllegalArgumentException::new);
        if (captor.getPowerSource() == PowerSource.SIMULATED) {
            return simulatedMeasureService.readMeasures(((SimulatedCaptor) captor),
                    Instant.now().minus(Duration.ofHours(Long.valueOf(nbHours))).truncatedTo(ChronoUnit.MINUTES),
                    Instant.now().truncatedTo(ChronoUnit.MINUTES),
                    MeasureStep.ONE_MINUTE)
                    .stream()
                    .map(m -> new MeasureDto(m.getInstant(),
                            m.getValueInWatt()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
