package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.ResultadoDTO;
import com.example.tpgrupo4ecotrack.Entity.HuellaCarbono;
import com.example.tpgrupo4ecotrack.Entity.Resultado;
import com.example.tpgrupo4ecotrack.Service.ResultadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resultados")
public class ResultadoController {

    private final ResultadoService resultadoService;
    public ResultadoController(ResultadoService resultadoService) {
        this.resultadoService = resultadoService;
    }

    @PostMapping("/resultado/generar/{huellaId}")
    public ResponseEntity<String> generarResultados(@PathVariable Long huellaId) {
        HuellaCarbono huella = new HuellaCarbono();
        huella.setIdHuella(huellaId);

        // ⚠️ Aquí deberías traer la huella desde tu repositorio si ya la tienes persistida
        // Ejemplo:
        // HuellaCarbono huella = huellaRepository.findById(huellaId)
        //         .orElseThrow(() -> new RuntimeException("Huella no encontrada"));

        resultadoService.generarResultadosPorHuella(huella);
        return ResponseEntity.ok("Resultados generados correctamente para la huella ID: " + huellaId);
    }

    // 🔹 Obtener todos los resultados de un usuario
    @GetMapping("/resultado/usuario/{usuarioId}")
    public ResponseEntity<List<Resultado>> obtenerResultadosPorUsuario(@PathVariable Long usuarioId) {
        List<Resultado> resultados = resultadoService.obtenerResultadosPorUsuario(usuarioId);
        return ResponseEntity.ok(resultados);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return resultadoService.eliminar(id);
    }
}
