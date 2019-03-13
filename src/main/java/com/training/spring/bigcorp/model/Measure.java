package com.training.spring.bigcorp.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Measure {
    @Id
    @GeneratedValue
    private String id;
    @Column(nullable=false)
    private Instant instant;
    @Column(nullable=false)
    private Integer valueInWatt;
    @ManyToOne(optional=false)
    private Captor captor;

    public Measure(){}

    public Measure(Instant instant, Integer valueInWatt, Captor captor) {
        this.instant = instant;
        this.valueInWatt = valueInWatt;
        this.captor = captor;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Integer getValueInWatt() {
        return valueInWatt;
    }

    public void setValueInWatt(Integer valueInWatt) {
        this.valueInWatt = valueInWatt;
    }

    public Captor getCaptor() {
        return captor;
    }

    public void setCaptor(Captor captor) {
        this.captor = captor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "instant=" + instant +
                ", valueInWatt=" + valueInWatt +
                ", captor=" + captor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measure measure = (Measure) o;
        return Objects.equals(instant, measure.instant) &&
                Objects.equals(valueInWatt, measure.valueInWatt) &&
                Objects.equals(captor, measure.captor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instant, valueInWatt, captor);
    }
}
