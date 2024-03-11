package com.example.apijwt;

import com.example.apijwt.entity.Plantacion;
import com.example.apijwt.entity.Sensor;
import com.example.apijwt.repository.PlantacionRepository;
import com.example.apijwt.repository.SensorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SensorCreationIntegrationTest {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private PlantacionRepository plantacionRepository;

    @Test
    public void testCreateSensorWithPlantacion() {
        // Asumimos que ya existe una plantación con ID 1 en la base de datos para este test
        Long plantacionId = 1L;
        Optional<Plantacion> plantacionOptional = plantacionRepository.findById(plantacionId);
        assertTrue(plantacionOptional.isPresent(), "Plantación no encontrada con id: " + plantacionId);

        Plantacion plantacion = plantacionOptional.get();

        Sensor sensor = new Sensor();
        sensor.setUbicacion("Ubicación de prueba");
        sensor.setFechaInstalacion(new Date());
        sensor.setPlantacion(plantacion);

        Sensor savedSensor = sensorRepository.save(sensor);
        assertNotNull(savedSensor.getId(), "El sensor creado debe tener un ID");

        // Limpieza: eliminar el sensor creado para mantener la base de datos en estado consistente
        sensorRepository.deleteById(savedSensor.getId());
    }
}
