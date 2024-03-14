package com.sensors.pulse.repository;


import com.sensors.pulse.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorRepo extends JpaRepository<Sensor, String> {
    @Query("SELECT s FROM Sensor s WHERE" +
            "(CAST(:before as string) IS NULL OR s.createdAt <= CAST(CAST(:before AS STRING) AS TIMESTAMP)) AND " +
            "(CAST(:after as string) IS NULL OR s.createdAt >= CAST(CAST(:after AS STRING) AS TIMESTAMP))")
    List<Sensor> getAllSensors(Timestamp before, Timestamp after);

    Optional<Sensor> findSensorById(UUID id);
}
