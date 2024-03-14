package com.sensors.pulse.service.impl;

import com.sensors.pulse.model.Measurement;
import com.sensors.pulse.model.Sensor;
import com.sensors.pulse.model.Type;
import com.sensors.pulse.repository.MeasurementRepo;
import com.sensors.pulse.service.MeasurementService;
import com.sensors.pulse.service.SensorService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class MeasurementsServiceImpl implements MeasurementService {

    private final MeasurementRepo measurementRepo;
    private final SensorService sensorService;

    public MeasurementsServiceImpl(MeasurementRepo measurementRepo, SensorService sensorService) {
        this.measurementRepo = measurementRepo;
        this.sensorService = sensorService;
    }

    @Override
    public Measurement addMeasurement(String sensorId, Timestamp timestamp, Type type, Float value) throws Exception {
        Sensor sensor = sensorService.getById(sensorId);
        if (sensor == null){
            throw new Exception("Sensor not found");
        }
        Measurement measurement = new Measurement(sensor, timestamp, type, value);
        return measurementRepo.save(measurement);
    }

    @Override
    public List<Measurement> getMeasurements(String sensorId, Timestamp before, Timestamp after) {
        return measurementRepo.getMeasurementByIdAndTimestamps(UUID.fromString(sensorId), before, after);
    }
}
