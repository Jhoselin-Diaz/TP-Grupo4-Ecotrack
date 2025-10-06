package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioViviendaDTO {
    private Long idServicios;
    private Double electricidadKWh;
    private Double gasNaturalM3;
    private Double gasolinaL;
    private Double carbonKl;
    private Double glpKl;
    private Double propanoKl;
    private Double emisionesKgCO2_S;
    private Boolean enviadoResultadoS;
    private Date fechaRegistro;
    private Long categoriaid;
    private Long factorid;
    private Long usuarioid;
}
