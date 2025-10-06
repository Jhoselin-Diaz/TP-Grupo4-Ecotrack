package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.ResultadoEquivalenciaDTO;
import com.example.tpgrupo4ecotrack.Entity.ResultadoEquivalencia;
import com.example.tpgrupo4ecotrack.Service.ResultadoEquivalenciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equivalencias")
public class ResultadoEquivalenciaController {

    private final ResultadoEquivalenciaService resultadoEquivalenciaService;
    public ResultadoEquivalenciaController(ResultadoEquivalenciaService resultadoEquivalenciaService) {
        this.resultadoEquivalenciaService = resultadoEquivalenciaService;
    }

    @GetMapping("/lista")
    public List<ResultadoEquivalenciaDTO> listar() {
        return resultadoEquivalenciaService.obtenerEquivalencias();
    }

    @PostMapping("/inserta")
    public ResultadoEquivalencia insertar(@RequestBody ResultadoEquivalenciaDTO dto) {
        return resultadoEquivalenciaService.insertar(dto);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return resultadoEquivalenciaService.eliminar(id);
    }
}
