package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SServicioViviendaDTO {
    private Long idServicios;
    private Float electricidadKWh;
    private Float gasNaturalM3;
    private Float gasolinaL;
    private Float carbonKl;
    private Float glpKl;
    private Float propanoKl;
    private Float emisionesKgCO2_S;
    private Boolean enviadoResultadoS;
    private LocalDateTime fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;
}
