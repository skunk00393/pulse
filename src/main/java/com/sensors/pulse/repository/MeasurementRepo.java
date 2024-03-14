package com.sensors.pulse.repository;

import com.sensors.pulse.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface MeasurementRepo extends JpaRepository<Measurement, Long> {
    
@Query("SELECT m FROM Measurement m WHERE " +
        "m.sensor.id = :sensorId AND " +
        "(CAST(:before as string) IS NULL OR m.timestamp <= CAST(CAST(:before AS STRING) AS TIMESTAMP)) AND " +
        "(CAST(:after as string) IS NULL OR m.timestamp >= CAST(CAST(:after AS STRING) AS TIMESTAMP))")
    List<Measurement> getMeasurementByIdAndTimestamps(UUID sensorId, Timestamp before, Timestamp after);
}
