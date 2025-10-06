package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimentoDTO {
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
