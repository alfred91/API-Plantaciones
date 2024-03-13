package com.example.apijwt.controller;

import com.example.apijwt.entity.Registro;
import com.example.apijwt.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @PostMapping("/registros/sensor/{sensorId}")
    public ResponseEntity<Registro> createRegistro(@PathVariable Long sensorId, @RequestBody Registro registro) {
        Registro nuevoRegistro = registroService.save(registro, sensorId);
        return ResponseEntity.ok(nuevoRegistro);
    }

    @GetMapping("/registros")
    public ResponseEntity<List<Registro>> getAllRegistros() {
        List<Registro> registros = registroService.findAll();
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/registros/{id}")
    public ResponseEntity<Registro> getRegistroById(@PathVariable Long id) {
        return registroService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/registros/{id}")
    public ResponseEntity<Registro> updateRegistro(@PathVariable Long id, @RequestBody Registro registroDetails) {
        return registroService.update(id, registroDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/registros/{id}")
    public ResponseEntity<Void> deleteRegistro(@PathVariable Long id) {
        if (registroService.deleteById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // OBRENER REGISTROS DE UN SENSOR EN UNA FECHA
    @GetMapping("/api/registros/sensor/{sensorId}/fecha/{fecha}")
    public ResponseEntity<List<Registro>> obtenerRegistrosPorSensorYFecha(
            @PathVariable Long sensorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<Registro> registros = registroService.findRegistrosBySensorIdAndFecha(sensorId, fecha);

        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(registros);
        }
    }

    //Mostrar la temperatura y humedad promedio de un sensor en un rango
    //de fechas, todas las lecturas:
    @GetMapping("/api/informes/sensor/{sensorId}/fechaInicio/{fi}/fechaFin/{ff}")
    public ResponseEntity<Map<String, Double>> obtenerPromedios(
            @PathVariable Long sensorId,
            @PathVariable("fi") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @PathVariable("ff") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        Map<String, Double> promedios = registroService.calcularPromedios(sensorId, fechaInicio, fechaFin);

        if (!promedios.isEmpty()) {
            return ResponseEntity.ok().body(promedios);
        } else {
            return ResponseEntity.ok().body(Map.of());
        }
    }

}