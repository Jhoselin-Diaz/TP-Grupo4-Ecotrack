package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.ResultadoDetalleDTO;
import com.example.tpgrupo4ecotrack.Entity.ResultadoDetalle;
import com.example.tpgrupo4ecotrack.Service.ResultadoDetalleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalles")
public class ResultadoDetalleController {

    private final ResultadoDetalleService resultadoDetalleService;
    public ResultadoDetalleController(ResultadoDetalleService resultadoDetalleService) {
        this.resultadoDetalleService = resultadoDetalleService;
    }

    @GetMapping("/lista")
    public List<ResultadoDetalleDTO> listar() {
        return resultadoDetalleService.obtenerDetalles();
    }

    @PostMapping("/inserta")
    public ResultadoDetalle insertar(@RequestBody ResultadoDetalleDTO dto) {
        return resultadoDetalleService.insertar(dto);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return resultadoDetalleService.eliminar(id);
    }
}
