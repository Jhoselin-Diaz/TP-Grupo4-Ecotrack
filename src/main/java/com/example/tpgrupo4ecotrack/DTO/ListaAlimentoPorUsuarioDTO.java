package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaAlimentoPorUsuarioDTO {
    private Long idAlimento;
    private String nombreAlimento;
    private Float cantidadKg;
    private Float emisionesKgCO2_AL;
    private Boolean enviadoResultadoAL;
    private Date fechaRegistro;
}
