package com.example.apijwt.repository;

import com.example.apijwt.entity.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    List<Registro> findBySensorIdAndFechaHoraBetween(Long sensorId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Registro> findBySensorId(Long sensorId);

    @Query("SELECT AVG(r.temperatura) as promedioTemperatura, AVG(r.humedad) as promedioHumedad " +
            "FROM Registro r WHERE r.sensor.id = :sensorId AND r.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    Optional<Object[]> findPromedioTemperaturaHumedadBySensorAndFechaBetween(@Param("sensorId") Long sensorId, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    // BUSCAR REGISTROS EN UNA FECHA ESPECÍFICA
    @Query("SELECT r FROM Registro r WHERE r.sensor.id = :sensorId AND r.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Registro> findRegistrosBySensorIdAndDay(@Param("sensorId") Long sensorId, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
