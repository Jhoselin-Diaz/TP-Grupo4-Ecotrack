package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaRopaDTO {
    private Long idRopa;
    private String tipoPrenda;
    private Float cantidadKg;
    private Float emisionesKgCO2_R;
    private Boolean enviadoResultadoR;
    private Date fechaRegistro;

}