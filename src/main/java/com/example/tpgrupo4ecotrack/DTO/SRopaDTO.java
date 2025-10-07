package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SRopaDTO {
    private Long idRopa;
    private String tipoPrenda;
    private Float cantidadKg;
    private Float emisionesKgCO2_R;
    private Boolean enviadoResultadoR;
    private LocalDateTime fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;
}