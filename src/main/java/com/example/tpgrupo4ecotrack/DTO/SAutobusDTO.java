package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SAutobusDTO {
    private Long idAutobus;
    private Float autobusKm;
    private Float autocarKm;
    private Float trenNacionalKm;
    private Float tranviaKm;
    private Float metroKm;
    private Float taxiKm;
    private String tipoGasolina;
    private Float emisionesKgCO2_A;
    private Boolean enviadoResultadoA;
    private LocalDateTime fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;
}
