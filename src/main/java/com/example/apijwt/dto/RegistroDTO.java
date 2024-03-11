package com.example.apijwt.dto;

import java.util.Date;

public record RegistroDTO(Long id, Date fechaHora, Double temperatura, Double humedad, Long sensorId) {
}
