package com.training.spring.bigcorp.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum PowerSource {
    FIXED,
    REAL,
    SIMULATED;


    @Enumerated(EnumType.STRING)
    static PowerSource of(String power) {
        if (power == null) {
            return null;
        }
        return PowerSource.valueOf(power);
    }
}
