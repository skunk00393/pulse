package com.sensors.pulse.service;

import com.sensors.pulse.model.Measurement;
import com.sensors.pulse.model.Type;

import java.sql.Timestamp;
import java.util.List;

public interface MeasurementService {

    Measurement addMeasurement(String sensorId, Timestamp timestamp, Type type, Float value) throws Exception;

    List<Measurement> getMeasurements(String sensorId, Timestamp before, Timestamp after);

}
