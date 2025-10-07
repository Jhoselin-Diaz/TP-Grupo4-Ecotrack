package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Service.SRopaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ropas")
public class SRopaController {

    private final SRopaService SRopaService;
    public SRopaController(SRopaService SRopaService) {
        this.SRopaService = SRopaService;
    }

    @GetMapping("/MisRopa")
    public ResponseEntity<List<ListaRopaDTO>> listarMisRopa(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListaRopaDTO> lista = SRopaService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/inserta")
    public ResponseEntity<SRopaDTO> insertar(@RequestBody SRopaDTO dto) {
        return new ResponseEntity<>(SRopaService.insertar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = SRopaService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return SRopaService.eliminar(id);
    }
}
