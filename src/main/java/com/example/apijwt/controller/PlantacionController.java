package com.example.apijwt.controller;

import com.example.apijwt.entity.Registro;
import com.example.apijwt.entity.Sensor;
import com.example.apijwt.repository.RegistroRepository;
import com.example.apijwt.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.apijwt.service.PlantacionService;
import com.example.apijwt.entity.Plantacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class PlantacionController {

    @Autowired
    private PlantacionService plantacionService;
    @Autowired
    private RegistroService registroService;

    @GetMapping("/plantaciones")
    public ResponseEntity<List<Plantacion>> findAll() {
        List<Plantacion> plantaciones = this.plantacionService.findAll();
        if (plantaciones.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(plantaciones);
    }

    @PostMapping("/plantaciones")
    public ResponseEntity<Plantacion> create(@RequestBody Plantacion plantacion) {
        this.plantacionService.save(plantacion);
        return ResponseEntity.ok(plantacion);
    }

    // MOSTRAR TODOS LOS SENSORES DE UNA PLANTACION
    @GetMapping("/api/plantacion/{id}")
    public ResponseEntity<Map<String, Object>> findByIdWithSensorsAndRegistros(@PathVariable Long id) {
        Optional<Plantacion> plantacionOpt = plantacionService.findById(id);
        if (!plantacionOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Plantacion plantacion = plantacionOpt.get();

        // Usar el nuevo método para obtener todos los registros asociados a la plantación
        List<Registro> registros = registroService.findRegistrosByPlantacionId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("plantacion", plantacion);
        response.put("registros", registros);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/plantacion/{idp}/fecha/{fecha}")
    public ResponseEntity<List<Registro>> obtenerRegistrosPorFecha(
            @PathVariable Long idp,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        LocalDateTime fechaInicio = fecha.atStartOfDay();
        LocalDateTime fechaFin = fecha.plusDays(1).atStartOfDay();

        Optional<Plantacion> plantacionOptional = plantacionService.findByIdWithSensors(idp);
        if (plantacionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Plantacion plantacion = plantacionOptional.get();
        List<Sensor> sensores = plantacion.getSensores();
        List<Registro> registros = new ArrayList<>();

        for (Sensor sensor : sensores) {
            List<Registro> registrosPorSensor = registroService.findRegistrosBySensorAndFechaBetween(
                    sensor.getId(), fechaInicio, fechaFin);
            registros.addAll(registrosPorSensor);
        }
        return ResponseEntity.ok(registros);
    }

//PROMEDIO EN UNA FECHA
    @GetMapping("/api/plantacion/{idp}/promedio/fecha/{fecha}")
    public ResponseEntity<Map<String, Object>> obtenerPromedioTemperaturaHumedadPorPlantacionYFecha(
            @PathVariable Long idp,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        Map<String, Object> promedios = registroService.calcularPromediosPorPlantacionYFecha(idp, fecha);

        if (promedios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(promedios);
    }
    //BORRAR UNA PLANTACION POR ID
    @DeleteMapping("/plantaciones/{id}")
    public ResponseEntity<Void> deletePlantacion(@PathVariable Long id) {
        Optional<Plantacion> plantacion = plantacionService.findById(id);
        if (!plantacion.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        plantacionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    // ACTUALIZAR UNA PLANTACION POR ID
    @PutMapping("/plantaciones/{id}")
    public ResponseEntity<Plantacion> updatePlantacion(@PathVariable Long id, @RequestBody Plantacion plantacionDetails) {
        Plantacion plantacion = plantacionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Plantación no encontrada con id: " + id));

        plantacion.setNombre(plantacionDetails.getNombre());
        plantacion.setLatitud(plantacionDetails.getLatitud());
        plantacion.setLongitud(plantacionDetails.getLongitud());

        Plantacion updatedPlantacion = plantacionService.save(plantacion);
        return ResponseEntity.ok(updatedPlantacion);
    }


}
