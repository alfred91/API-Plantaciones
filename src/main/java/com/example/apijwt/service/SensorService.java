package com.example.apijwt.service;

import com.example.apijwt.entity.Sensor;
import com.example.apijwt.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    public Sensor save(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    public Optional<Sensor> findById(Long id) {
        return sensorRepository.findById(id);
    }

    public Optional<Sensor> update(Long id, Sensor sensorDetails) {
        return sensorRepository.findById(id).map(sensor -> {
            sensor.setUbicacion(sensorDetails.getUbicacion());
            sensor.setFechaInstalacion(sensorDetails.getFechaInstalacion());
            sensor.setPlantacion(sensorDetails.getPlantacion());
            return sensorRepository.save(sensor);
        });
    }

    public boolean deleteById(Long id) {
        return sensorRepository.findById(id).map(sensor -> {
            sensorRepository.delete(sensor);
            return true;
        }).orElse(false);
    }
}
