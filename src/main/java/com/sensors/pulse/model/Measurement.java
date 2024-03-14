package com.sensors.pulse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Entity
@NoArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sensor_id", nullable=false)
    private Sensor sensor;

    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Float value;

    public Measurement(Sensor sensor, Timestamp timestamp, Type type, Float value){
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
    }
}
