package com.example.apijwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String ubicacion;
    private LocalDateTime fechaInstalacion;


    @ManyToOne
    @JoinColumn(name = "plantacion_id", nullable = false)
    private Plantacion plantacion;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Registro> registros;
}
