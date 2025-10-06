package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectrodomesticoDTO {
    private Long idElectrodomestico;
    private String tipoElectrodomestico;
    private Double consumoKWh;
    private Double emisionesKgCO2_E;
    private Boolean enviadoResultadoE;
    private Date fechaRegistro;
    private Long categoria;
    private Long factor;
    private Long usuario;
}
