package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Service.SRopaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ropas")
public class SRopaController {

    private final SRopaService sRopaService;
    public SRopaController(SRopaService SRopaService) {

        this.sRopaService = SRopaService;
    }

    @GetMapping("/listaAdmin")
    public List<SRopaDTO> listar() {

        return sRopaService.listaRopaAdmin();
    }

    @PostMapping("/registrar")
    public SRopaDTO registrarRopa(@RequestBody SRopaCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return sRopaService.Registrar(dto, username);
    }

    @GetMapping("/MisRopa")
    public ResponseEntity<List<ListaRopaDTO>> listarMisRopa(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListaRopaDTO> lista = sRopaService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<SRopaDTO> actualizarRopa(
            @PathVariable Long id,
            @RequestBody SRopaCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        SRopaDTO result = sRopaService.actualizar(id, dto, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = sRopaService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return sRopaService.eliminar(id);
    }
}
