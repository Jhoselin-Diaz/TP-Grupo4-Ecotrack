package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SAlimentoDTO {
    private Long idAlimento;
    private String nombreAlimento;
    private Float cantidadKg;
    private Float emisionesKgCO2_AL;
    private Boolean enviadoResultadoAL;
    private LocalDateTime fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;
}
