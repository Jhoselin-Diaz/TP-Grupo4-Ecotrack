package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
  ResultadoDTO contiene el resumen por categoria más los detalles y equivalencias
*/
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResultadoDTO {
    private Long idResultado;
    private Long huellaId;
    private Long categoriaId;
    private List<ResultadoDetalleDTO> detalles;
    private List<ResultadoEquivalenciaDTO> equivalencias;
}
