package com.example.apijwt.controller;

import com.example.apijwt.dto.SensorDTO;
import com.example.apijwt.entity.Plantacion;
import com.example.apijwt.entity.Sensor;
import com.example.apijwt.repository.PlantacionRepository;
import com.example.apijwt.service.SensorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private PlantacionRepository plantacionRepository;

    @PostMapping("/sensores")
    public ResponseEntity<Sensor> createSensor(@RequestBody SensorDTO sensorDTO) {
        Plantacion plantacion = plantacionRepository.findById(sensorDTO.plantacionId())
                .orElseThrow(() -> new EntityNotFoundException("Plantación no encontrada con id: " + sensorDTO.plantacionId()));

        Sensor sensor = new Sensor();
        sensor.setUbicacion(sensorDTO.ubicacion());
        sensor.setFechaInstalacion(sensorDTO.fechaInstalacion());
        sensor.setPlantacion(plantacion);

        Sensor nuevoSensor = sensorService.save(sensor);
        return ResponseEntity.ok(nuevoSensor);
    }

    @GetMapping("/sensores")
    public ResponseEntity<List<Sensor>> getAllSensors() {
        List<Sensor> sensores = sensorService.findAll();
        return ResponseEntity.ok(sensores);
    }
    @GetMapping("/test")
    public ResponseEntity<String> testSensorEndpoint() {
        return ResponseEntity.ok("El endpoint de sensores está funcionando correctamente.");
    }

    @GetMapping("/sensores/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        return sensorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/sensores/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Long id, @RequestBody Sensor sensorDetails) {
        return sensorService.update(id, sensorDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sensores/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        if (sensorService.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
