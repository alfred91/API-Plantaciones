package com.example.apijwt.repository;

import com.example.apijwt.entity.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    List<Registro> findBySensorIdAndFechaHoraBetween(Long sensorId, Date fechaInicio, Date fechaFin);

    @Query("SELECT AVG(r.temperatura) as temperaturaPromedio, AVG(r.humedad) as humedadPromedio FROM Registro r WHERE r.sensor.id = :sensorId AND r.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    Object findPromedioTemperaturaHumedadBySensorIdAndFechaHoraBetween(Long sensorId, Date fechaInicio, Date fechaFin);
}
