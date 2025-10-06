package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "factores_emision")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactorEmision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactor;

    @Column(nullable = false)
    private String codigo;

    private String unidad;

    private Float factorKgCO2PorUnidad;
}
