package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Service.SServicioViviendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class SServicioViviendaController {

    private final SServicioViviendaService sServicioViviendaService;
    public SServicioViviendaController(SServicioViviendaService sServicioViviendaService) {
        this.sServicioViviendaService = sServicioViviendaService;
    }

    @GetMapping("/MisServicio")
    public ResponseEntity<List<ListarServicioDTO>> listarMisServicio(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListarServicioDTO> lista = sServicioViviendaService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }


    @PostMapping("/registrar")
    public List<SServicioViviendaDTO> registrarAlimento(@RequestBody SServicioCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return sServicioViviendaService.Registrar(dto, username);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<SServicioViviendaDTO> actualizarCoche(
            @PathVariable Long id,
            @RequestBody SServicioCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        SServicioViviendaDTO result = sServicioViviendaService.actualizar(id, dto, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = sServicioViviendaService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return sServicioViviendaService.eliminar(id);
    }
}
