package com.sensors.pulse;

import com.pulse.messages.Messages;
import com.sensors.pulse.model.Measurement;
import com.sensors.pulse.model.Sensor;
import com.sensors.pulse.model.Type;
import com.sensors.pulse.service.MeasurementService;
import com.sensors.pulse.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class PulseApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private SensorService sensorService;

    private Sensor sensor;

    //This is done poorly, needs to be run only once
    @BeforeEach
    public void setup(WebApplicationContext wac) throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        initData();
    }

    private void initData() throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp later = new Timestamp(System.currentTimeMillis() + 60000);
        sensor = sensorService.addSensor("newSensor");
        measurementService.addMeasurement(sensor.getId().toString(), now, Type.TEMPERATURE, 20.5f);
        measurementService.addMeasurement(sensor.getId().toString(), now, Type.PM10, 15.5f);
        measurementService.addMeasurement(sensor.getId().toString(), later, Type.TEMPERATURE, 19.2f);
    }

    @Test
    void getSensor_found() throws Exception {
        mvc.perform(get("/sensor/list")
                        .param("before", new Timestamp(System.currentTimeMillis()).toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"));
    }

    @Test
    void getSensor_notFound() throws Exception {
        mvc.perform(get("/sensor/list")
                        .param("before", new Timestamp(System.currentTimeMillis()).toString())
                        .param("after", new Timestamp(System.currentTimeMillis() + 100).toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"));
    }

    @Test
    void addSensor() throws Exception {
        mvc.perform(post("/sensor/new").contentType(MediaType.APPLICATION_JSON)
                        .param("name", "newSensor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"));
    }

    @Test
    void addMeasurement() throws Exception {
        mvc.perform(post("/measurement/new")
                        .param("sensorId", sensor.getId().toString())
                        .param("timestamp", new Timestamp(System.currentTimeMillis()).toString())
                        .param("type", "TEMPERATURE")
                        .param("value", String.valueOf(15.1f)))
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    void getMeasurement_found() throws Exception {
        MvcResult result = mvc.perform(get("/measurement/list/" + sensor.getId().toString()))
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn();

        Messages.MeasurementList measurementList = Messages.MeasurementList.parseFrom(result.getResponse().getContentAsByteArray());
        List<Measurement> toCompare = measurementService.getMeasurements(measurementList.getMeasurements(0).getSensorId(), null, null);
        measurementList.getMeasurementsList().forEach(measurement -> {
            Measurement found = toCompare.stream().filter(elem -> elem.getId() == measurement.getId()).findFirst().orElse(null);
            if (found == null) {
                fail("Measurement not found");
            }
            assertEquals(measurement.getSensorId(), found.getSensor().getId().toString());
            assertEquals(measurement.getTimestamp(), found.getTimestamp().toString());
            assertEquals(measurement.getType(), found.getType().toString());
            assertEquals(measurement.getValue(), found.getValue(), 0.0001f);
        });
    }

    @Test
    void getMeasurement_notFound() throws Exception {
        mvc.perform(get("/measurement/list/" + sensor.getId().toString())
                        .param("before", new Timestamp(System.currentTimeMillis()).toString())
                        .param("after", new Timestamp(System.currentTimeMillis() + 100).toString()))
                .andExpect(content().contentType("application/x-protobuf;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
