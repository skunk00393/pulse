package com.sensors.pulse.service.impl;

import com.sensors.pulse.model.Sensor;
import com.sensors.pulse.repository.SensorRepo;
import com.sensors.pulse.service.SensorService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepo sensorRepo;

    public SensorServiceImpl(SensorRepo sensorRepo) {
        this.sensorRepo = sensorRepo;
    }

    @Override
    public Sensor addSensor(String name) {
        Sensor sensor = new Sensor(name);
        return sensorRepo.save(sensor);
    }

    @Override
    public List<Sensor> getAllSensors(Timestamp before, Timestamp after) {
        return sensorRepo.getAllSensors(before, after);
    }

    @Override
    public Sensor getById(String sensorId) {
        Optional<Sensor> sensor = sensorRepo.findSensorById(UUID.fromString(sensorId));
        return sensor.orElse(null);
    }
}
