package com.example.apijwt.service;

import com.example.apijwt.entity.Registro;
import com.example.apijwt.entity.Sensor;
import com.example.apijwt.repository.RegistroRepository;
import com.example.apijwt.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;
    @Autowired
    private SensorRepository sensorRepository;

    public Registro save(Registro registro, Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor no encontrado con id: " + sensorId));
        registro.setSensor(sensor);
        return registroRepository.save(registro);
    }

    public List<Registro> findAll() {
        return registroRepository.findAll();
    }

    public Optional<Registro> findById(Long id) {
        return registroRepository.findById(id);
    }

    public Optional<Registro> update(Long id, Registro registroDetails) {
        return registroRepository.findById(id).map(registro -> {
            registro.setFechaHora(registroDetails.getFechaHora());
            registro.setTemperatura(registroDetails.getTemperatura());
            registro.setHumedad(registroDetails.getHumedad());
            return registroRepository.save(registro);
        });
    }

    //CALCULAR LA MEDIA DE LOS VALORES TEMP. Y HUMEDAD PARA PASARLOS AL CONTROLADOR
    public Map<String, Double> calcularPromedios(Long sensorId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Registro> registros = findRegistrosBySensorAndFechaBetween(sensorId, fechaInicio, fechaFin);
        double sumaTemperatura = 0;
        double sumaHumedad = 0;
        int totalRegistros = registros.size();

        for (Registro registro : registros) {
            sumaTemperatura += registro.getTemperatura();
            sumaHumedad += registro.getHumedad();
        }

        double promedioTemperatura = sumaTemperatura / totalRegistros;
        double promedioHumedad = sumaHumedad / totalRegistros;

        Map<String, Double> promedios = new HashMap<>();
        promedios.put("promedioTemperatura", promedioTemperatura);
        promedios.put("promedioHumedad", promedioHumedad);

        return promedios;
    }


    public boolean deleteById(Long id) {
        return registroRepository.findById(id).map(registro -> {
            registroRepository.delete(registro);
            return true;
        }).orElse(false);
    }

    public Optional<Object[]> findPromedioTemperaturaHumedadBySensorAndFechaBetween(Long sensorId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return registroRepository.findPromedioTemperaturaHumedadBySensorAndFechaBetween(sensorId, fechaInicio, fechaFin);
    }

    public List<Registro> findRegistrosBySensorAndFechaBetween(Long sensorId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return registroRepository.findBySensorIdAndFechaHoraBetween(sensorId, fechaInicio, fechaFin);
    }

    public Map<String, Object> calcularPromediosPorPlantacionYFecha(Long plantacionId, LocalDate fecha) {
        List<Sensor> sensores = sensorRepository.findByPlantacionId(plantacionId);
        LocalDateTime fechaInicio = fecha.atStartOfDay();
        LocalDateTime fechaFin = fecha.plusDays(1).atStartOfDay();

        double sumaTemperatura = 0;
        double sumaHumedad = 0;
        int contadorRegistros = 0;

        for (Sensor sensor : sensores) {
            List<Registro> registros = registroRepository.findBySensorIdAndFechaHoraBetween(sensor.getId(), fechaInicio, fechaFin);
            for (Registro registro : registros) {
                sumaTemperatura += registro.getTemperatura();
                sumaHumedad += registro.getHumedad();
                contadorRegistros++;
            }
        }
        Map<String, Object> promedios = new HashMap<>();
        if (contadorRegistros > 0) {
            promedios.put("promedioTemperatura", sumaTemperatura / contadorRegistros);
            promedios.put("promedioHumedad", sumaHumedad / contadorRegistros);
            promedios.put("fecha", fecha);
            promedios.put("plantacionId", plantacionId);
        }

        return promedios;
    }

    public List<Registro> findRegistrosByPlantacionId(Long plantacionId) {
        List<Sensor> sensores = sensorRepository.findByPlantacionId(plantacionId);
        List<Registro> registrosTotales = new ArrayList<>();
        for (Sensor sensor : sensores) {
            List<Registro> registrosPorSensor = registroRepository.findBySensorId(sensor.getId());
            registrosTotales.addAll(registrosPorSensor);
        }
        return registrosTotales;
    }

    public List<Registro> findRegistrosBySensorIdAndFecha(Long sensorId, LocalDate fecha) {
        LocalDateTime fechaInicio = fecha.atStartOfDay();
        LocalDateTime fechaFin = fecha.plusDays(1).atStartOfDay();
        return registroRepository.findBySensorIdAndFechaHoraBetween(sensorId, fechaInicio, fechaFin);
    }

    //CALCULAR LA MEDIA HISTÓRICA
    public Map<String, Double> calcularMediaHistorica(Long sensorId) {
        List<Registro> registros = registroRepository.findBySensorId(sensorId);
        double sumaTemperatura = 0;
        double sumaHumedad = 0;
        int totalRegistros = registros.size();

        for (Registro registro : registros) {
            sumaTemperatura += registro.getTemperatura();
            sumaHumedad += registro.getHumedad();
        }

        Map<String, Double> medias = new HashMap<>();
        if (totalRegistros > 0) {
            medias.put("mediaTemperatura", sumaTemperatura / totalRegistros);
            medias.put("mediaHumedad", sumaHumedad / totalRegistros);
        } else {
            medias.put("mediaTemperatura", 0.0);
            medias.put("mediaHumedad", 0.0);
        }

        return medias;
    }

}
