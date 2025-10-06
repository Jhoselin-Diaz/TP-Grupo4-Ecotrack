package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuellaDTO {

    private Long idHuella;
    private String periodo;
    private LocalDateTime fechaCalculo;
    private Long totalKgCO2;
    private Long usuarioId;
    private List<Long> resultadoIds;

    public HuellaDTO(LocalDateTime fechaCalculo, Long usuarioId) {
        this.fechaCalculo = fechaCalculo;
        this.usuarioId = usuarioId;
    }

}