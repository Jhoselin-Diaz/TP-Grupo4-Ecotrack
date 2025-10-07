package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SCocheDTO {
    private Long idCoche;
    private Integer numeroCoches;
    private Float kilometrajeTotal;
    private String marca;
    private String tipoGasolina;
    private Float emisionesKgCO2_C;
    private Boolean enviadoResultadoC;
    private LocalDateTime fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;


}
