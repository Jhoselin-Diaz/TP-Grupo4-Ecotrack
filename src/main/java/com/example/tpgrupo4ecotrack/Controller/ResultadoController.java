package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.ResultadoDTO;
import com.example.tpgrupo4ecotrack.Entity.Resultado;
import com.example.tpgrupo4ecotrack.Service.ResultadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resultados")
public class ResultadoController {

    private final ResultadoService resultadoService;
    public ResultadoController(ResultadoService resultadoService) {
        this.resultadoService = resultadoService;
    }

    @GetMapping("/lista")
    public List<ResultadoDTO> listar() {
        return resultadoService.obtenerResultados();
    }

    @PostMapping("/inserta")
    public Resultado insertar(@RequestBody ResultadoDTO dto) {
        return resultadoService.insertar(dto);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return resultadoService.eliminar(id);
    }
}
