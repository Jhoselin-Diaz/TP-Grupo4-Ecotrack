package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.ResultadoDetalleDTO;
import com.example.tpgrupo4ecotrack.Entity.ResultadoDetalle;
import com.example.tpgrupo4ecotrack.Repository.ResultadoDetalleRepository;
import com.example.tpgrupo4ecotrack.Service.ResultadoDetalleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalles")
public class ResultadoDetalleController {

    private final ResultadoDetalleService resultadoDetalleService;
    private final ResultadoDetalleRepository resultadoDetalleRepository;

    public ResultadoDetalleController(ResultadoDetalleService resultadoDetalleService, ResultadoDetalleRepository resultadoDetalleRepository) {
        this.resultadoDetalleService = resultadoDetalleService;
        this.resultadoDetalleRepository = resultadoDetalleRepository;
    }

    @GetMapping("/obtener")
    public ResponseEntity<List<ResultadoDetalle>> obtenerTodos() {
        return ResponseEntity.ok(resultadoDetalleRepository.findAll());
    }

    // 🔹 Obtener detalles por ID de resultado
    @GetMapping("/obtenerResultados/{resultadoId}")
    public ResponseEntity<List<ResultadoDetalle>> obtenerPorResultado(@PathVariable Long resultadoId) {
        return ResponseEntity.ok(resultadoDetalleRepository.findByResultado_IdResultado(resultadoId));
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return resultadoDetalleService.eliminar(id);
    }
}
