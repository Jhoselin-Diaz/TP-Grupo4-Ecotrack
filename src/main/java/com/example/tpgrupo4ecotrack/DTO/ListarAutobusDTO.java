package com.example.tpgrupo4ecotrack.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListarAutobusDTO {
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
}
