package com.sensors.pulse.controller;

import com.pulse.messages.Messages;
import com.sensors.pulse.model.Sensor;
import com.sensors.pulse.service.SensorService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("sensor")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /*
        Creates a new sensor
     */
    @PostMapping("/new")
    Messages.SensorCreated addSensor(@RequestParam("name") String name){
        Sensor sensor = sensorService.addSensor(name);
        Messages.SensorCreated.Builder sensorBuilder = Messages.SensorCreated.newBuilder();
        sensorBuilder.setId(sensor.getId().toString());
        return sensorBuilder.build();
    }

    /*
        Lists all sensor names created either before timestamp, after timestamp, both or neither
     */
    @GetMapping("/list")
    Messages.SensorList getSensors(@RequestParam(required = false) Timestamp before,
                                   @RequestParam(required = false) Timestamp after) {
        List<Sensor> sensorList = sensorService.getAllSensors(before, after);
        Messages.SensorList.Builder sensorListBuilder = Messages.SensorList.newBuilder();
        Messages.Sensor.Builder sensorBuilder = Messages.Sensor.newBuilder();
        sensorList.forEach(sensor -> {
            sensorBuilder.setId(sensor.getId().toString());
            sensorBuilder.setName(sensor.getName());
            sensorBuilder.setTimestamp(sensor.getCreatedAt().toString());
            sensorListBuilder.addSensors(sensorBuilder.build());
        });
        return sensorListBuilder.build();
    }
}
