package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAutobus;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaServicioVivienda;
import com.example.tpgrupo4ecotrack.Service.ServicioViviendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioViviendaController {

    private final ServicioViviendaService servicioViviendaService;
    public ServicioViviendaController(ServicioViviendaService servicioViviendaService) {
        this.servicioViviendaService = servicioViviendaService;
    }

    @GetMapping("/MisServicio")
    public ResponseEntity<List<ListarServicioDTO>> listarMisServicio(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListarServicioDTO> lista = servicioViviendaService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }


    @PostMapping("/inserta")
    public ResponseEntity<ServicioViviendaDTO> insertar(@RequestBody ServicioViviendaDTO dto) {
        return new ResponseEntity<>(servicioViviendaService.insertar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = servicioViviendaService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return servicioViviendaService.eliminar(id);
    }
}
