package com.example.apijwt.controller;

import com.example.apijwt.entity.Plantacion;
import com.example.apijwt.entity.Registro;
import com.example.apijwt.entity.Sensor;
import com.example.apijwt.repository.PlantacionRepository;
import com.example.apijwt.service.RegistroService;
import com.example.apijwt.service.SensorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class SensorController {

    @Autowired
    private SensorService sensorService;
    @Autowired
    private PlantacionRepository plantacionRepository;
    @Autowired
    private RegistroService registroService;

    //ENDPOINT PARA VER TODOS LOS SENSORES:
    @GetMapping("/sensores")
    public ResponseEntity<List<Sensor>> getAllSensors() {
        List<Sensor> sensores = sensorService.findAll();
        return ResponseEntity.ok(sensores);
    }

    //OBTENER UN SENSOR POR ID:
    @GetMapping("/sensores/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        return sensorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //CREAR UN SENSOR:
    @PostMapping("/sensores/plantacion/{plantacionId}")
    public ResponseEntity<Sensor> createSensor(@PathVariable Long plantacionId, @RequestBody Sensor sensor) {
        Plantacion plantacion = plantacionRepository.findById(plantacionId)
                .orElseThrow(() -> new EntityNotFoundException("Plantación no encontrada con id: " + plantacionId));
        sensor.setPlantacion(plantacion);

        Sensor nuevoSensor = sensorService.save(sensor);
        return ResponseEntity.ok(nuevoSensor);
    }

    // OBTENER REGISTROS POR SENSOR Y FECHA:
    @GetMapping("/api/sensor/{ids}/fecha/{fecha}")
    public ResponseEntity<List<Registro>> obtenerRegistrosPorSensorYFecha(
            @PathVariable Long ids,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<Registro> registros = registroService.findRegistrosBySensorIdAndFecha(ids, fecha);

        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(registros);
    }

    // DEVOLVER LAS MEDIAS HISTÓRICAS DE UN SENSOR:
    @GetMapping("/api/sensor/{ids}/media")
    public ResponseEntity<Map<String, Double>> obtenerMediaHistoricaPorSensor(@PathVariable Long ids) {
        Map<String, Double> medias = registroService.calcularMediaHistorica(ids);

        if (medias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(medias);
    }
    // ACTUALIZAR UN SENSOR POR SU ID:
    @PutMapping("/sensores/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Long id, @RequestBody Sensor sensorDetails) {
        return sensorService.update(id, sensorDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // BORRAR UN SENSOR POR SU ID:
    @DeleteMapping("/sensores/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        if (sensorService.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testSensorEndpoint() {
        return ResponseEntity.ok("El endpoint de sensores está funcionando correctamente.");
    }
}
