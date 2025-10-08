package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListarElectDTO {
    private Long idElectrodomestico;
    private String tipoElectrodomestico;
    private Float consumoKWh;
    private Float emisionesKgCO2_E;
    private Boolean enviadoResultadoE;
    private LocalDateTime fechaRegistro;
}
