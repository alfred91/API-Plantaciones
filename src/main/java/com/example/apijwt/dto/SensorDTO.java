package com.example.apijwt.dto;

import java.util.Date;

public record SensorDTO(Long id, String ubicacion, Date fechaInstalacion, Long plantacionId) {
}
