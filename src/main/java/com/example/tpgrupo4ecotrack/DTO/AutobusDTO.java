package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutobusDTO {
    private Long idAutobus;
    private Double autobusKm;
    private Double autocarKm;
    private Double trenNacionalKm;
    private Double tranviaKm;
    private Double metroKm;
    private Double taxiKm;
    private Double emisionesKgCO2_A;
    private Boolean enviadoResultadoA;
    private Date fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;
}
