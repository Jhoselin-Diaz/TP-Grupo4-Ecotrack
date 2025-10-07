package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Service.SServicioViviendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class SServicioViviendaController {

    private final SServicioViviendaService SServicioViviendaService;
    public SServicioViviendaController(SServicioViviendaService SServicioViviendaService) {
        this.SServicioViviendaService = SServicioViviendaService;
    }

    @GetMapping("/MisServicio")
    public ResponseEntity<List<ListarServicioDTO>> listarMisServicio(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListarServicioDTO> lista = SServicioViviendaService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }


    @PostMapping("/inserta")
    public ResponseEntity<SServicioViviendaDTO> insertar(@RequestBody SServicioViviendaDTO dto) {
        return new ResponseEntity<>(SServicioViviendaService.insertar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = SServicioViviendaService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return SServicioViviendaService.eliminar(id);
    }
}
