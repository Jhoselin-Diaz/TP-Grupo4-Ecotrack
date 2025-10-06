package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDetalleDTO {
    private Long idDetalle;
    private Double emisionesKgCO2;
    private Long resultadoId;

    private Long servicioId;
    private Long cocheId;
    private Long autobusId;
    private Long alimentoId;
    private Long electrodomesticoId;
    private Long ropaId;

}
