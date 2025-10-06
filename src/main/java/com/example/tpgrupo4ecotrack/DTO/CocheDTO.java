package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CocheDTO {
    private Long idCoche;
    private Integer numeroCoches;
    private Double kilometrajeTotal;
    private String marca;
    private String tipoGasolina;
    private Double emisionesKgCO2_C;
    private Boolean enviadoResultadoC;
    private Date fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;
}
