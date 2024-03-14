package com.sensors.pulse.service;

import com.sensors.pulse.model.Sensor;

import java.sql.Timestamp;
import java.util.List;

public interface SensorService {

    Sensor addSensor(String name);

    List<Sensor> getAllSensors(Timestamp before, Timestamp after);

    Sensor getById(String sensorId);

}
