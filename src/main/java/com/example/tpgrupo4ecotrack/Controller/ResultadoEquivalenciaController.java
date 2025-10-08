package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.ResultadoEquivalenciaDTO;
import com.example.tpgrupo4ecotrack.Entity.ResultadoEquivalencia;
import com.example.tpgrupo4ecotrack.Repository.ResultadoEquivalenciaRepository;
import com.example.tpgrupo4ecotrack.Service.ResultadoEquivalenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equivalencias")
public class ResultadoEquivalenciaController {

    private final ResultadoEquivalenciaService resultadoEquivalenciaService;
    private final ResultadoEquivalenciaRepository resultadoEquivalenciaRepository;

    public ResultadoEquivalenciaController(ResultadoEquivalenciaService resultadoEquivalenciaService, ResultadoEquivalenciaRepository resultadoEquivalenciaRepository) {
        this.resultadoEquivalenciaService = resultadoEquivalenciaService;
        this.resultadoEquivalenciaRepository = resultadoEquivalenciaRepository;
    }

    // 🔹 Obtener todas las equivalencias
    @GetMapping("/obtener")
    public ResponseEntity<List<ResultadoEquivalencia>> obtenerTodas() {
        return ResponseEntity.ok(resultadoEquivalenciaRepository.findAll());
    }

    // 🔹 Obtener equivalencias por resultado
    @GetMapping("/resultado/{resultadoId}")
    public ResponseEntity<List<ResultadoEquivalencia>> obtenerPorResultado(@PathVariable Long resultadoId) {
        return ResponseEntity.ok(resultadoEquivalenciaRepository.findByResultado_IdResultado(resultadoId));
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return resultadoEquivalenciaService.eliminar(id);
    }
}
