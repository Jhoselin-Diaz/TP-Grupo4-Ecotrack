package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactorEmisionDTO {
    private Long idFactor;
    private String codigo;
    private String unidad;
    private Float factorKgCO2PorUnidad;
}