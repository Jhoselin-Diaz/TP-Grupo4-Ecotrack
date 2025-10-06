package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoEquivalenciaDTO {
    private Long idEquivalencia;
    private String tipoEquivalencia;
    private Double valorEquivalente;
    private String unidad;
    private Long resultadoId;
}
