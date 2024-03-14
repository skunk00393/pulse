package com.sensors.pulse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Sensor {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Getter
    private String name;
    private Timestamp createdAt;
    @OneToMany(mappedBy = "sensor")
    private Set<Measurement> measurements;

    public Sensor(String name) {
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

}
