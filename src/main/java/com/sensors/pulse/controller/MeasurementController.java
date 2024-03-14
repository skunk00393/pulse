package com.sensors.pulse.controller;

import com.pulse.messages.Messages;
import com.sensors.pulse.model.Measurement;
import com.sensors.pulse.model.Type;
import com.sensors.pulse.service.MeasurementService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("measurement")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    /*
        Adds a new measurement by its type only if the sensor exists
    */
    @PostMapping("/new")
    Messages.Measurement addMeasurement(@RequestParam("sensorId") String sensorId,
                                        @RequestParam("timestamp") Timestamp timestamp,
                                        @RequestParam("type") Type type,
                                        @RequestParam("value") Float value) throws Exception {
        Measurement measurement = measurementService.addMeasurement(sensorId, timestamp, type, value);
        Messages.Measurement.Builder measurementBuilder = Messages.Measurement.newBuilder();
        measurementBuilder.setSensorId(sensorId);
        measurementBuilder.setTimestamp(measurement.getTimestamp().toString());
        measurementBuilder.setType(measurement.getType().toString());
        measurementBuilder.setValue(measurement.getValue());
        return measurementBuilder.build();
    }

    /*
        Lists all the measurements by a sensor measured either before timestamp, after timestamp, both or neither
    */
    @GetMapping("/list/{sensorId}")
    Messages.MeasurementList listMeasurements(@PathVariable String sensorId,
                                              @RequestParam(required = false) Timestamp before,
                                              @RequestParam(required = false) Timestamp after){
        List<Measurement> measurementList = measurementService.getMeasurements(sensorId, before, after);
        Messages.Measurement.Builder measurementBuilder = Messages.Measurement.newBuilder();
        Messages.MeasurementList.Builder measurementListBuilder = Messages.MeasurementList.newBuilder();
        measurementList.forEach(measurement -> {
            measurementBuilder.setId(measurement.getId());
            measurementBuilder.setSensorId(sensorId);
            measurementBuilder.setTimestamp(measurement.getTimestamp().toString());
            measurementBuilder.setType(measurement.getType().toString());
            measurementBuilder.setValue(measurement.getValue());
            measurementListBuilder.addMeasurements(measurementBuilder.build());
        });
        return measurementListBuilder.build();
    }

}
