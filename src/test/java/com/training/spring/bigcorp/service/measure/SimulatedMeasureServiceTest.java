package com.training.spring.bigcorp.service.measure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.spring.bigcorp.controller.dto.MeasureDto;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.MeasureStep;
import com.training.spring.bigcorp.model.SimulatedCaptor;
import com.training.spring.bigcorp.model.Site;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@RunWith(SpringRunner.class)
@RestClientTest(SimulatedMeasureService.class)
public class SimulatedMeasureServiceTest {
    @Autowired
    private SimulatedMeasureService service;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void readMeasures() throws Exception {
        Instant start = Instant.parse("2018-09-01T22:00:00Z");
        Instant end = Instant.parse("2018-09-01T22:30:00Z");
        List<MeasureDto> expectedMeasures = Arrays.asList(
                new MeasureDto(Instant.parse("2018-09-01T22:00:00Z"), 1234),
                new MeasureDto(Instant.parse("2018-09-01T22:30:00Z"), 4567)
        );
        String expectedJson = objectMapper.writeValueAsString(expectedMeasures);
        String request = "http://localhost:8090/measures?start=2018-09-01T22:00:00Z&"+"end=2018-09-01T22:30:00Z&min=500000&max=1000000&step=3600";
        this.server.expect(MockRestRequestMatchers.requestTo(request))
                .andRespond(MockRestResponseCreators.withSuccess(expectedJson, MediaType.APPLICATION_JSON));
        List<Measure> measures = service.readMeasures(new SimulatedCaptor("Capteur test", new Site("BigCorp Lyon"), 500000, 1000000), start, end, MeasureStep.ONE_HOUR);
        assertThat(measures).hasSize(2);
        assertThat(measures)
                .extracting("instant", "valueInWatt")
                .containsExactly(
                        tuple(Instant.parse("2018-09-01T22:00:00Z"),1234),
                        tuple(Instant.parse("2018-09-01T22:30:00Z"),4567)
                );
    }
}