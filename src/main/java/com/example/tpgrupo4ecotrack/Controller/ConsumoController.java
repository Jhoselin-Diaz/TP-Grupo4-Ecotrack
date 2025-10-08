package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.ConsumoDTO;
import com.example.tpgrupo4ecotrack.Service.ConsumoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/consumos")
public class ConsumoController {

    private final ConsumoService consumoService;
    public ConsumoController(ConsumoService consumoService) {
        this.consumoService = consumoService; }

    @GetMapping("/mostrar/{usuarioId}")
    public ResponseEntity<List<ConsumoDTO>> getConsumos(
            @PathVariable Long usuarioId,
            @RequestParam("fechaInicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam("fechaFin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<ConsumoDTO> consumos = consumoService.getConsumos(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(consumos);
    }
}
