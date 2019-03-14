package com.training.spring.bigcorp.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SIMULATED")
public class SimulatedCaptor extends Captor  {
    private Integer minPowerInWatt;
    private Integer maxPowerInWatt;

    @Deprecated
    public SimulatedCaptor() {
        super();
        // used only by serializer and deserializer
    }
    public SimulatedCaptor(String name, Site site) {
        super(name, site);
    }
}
