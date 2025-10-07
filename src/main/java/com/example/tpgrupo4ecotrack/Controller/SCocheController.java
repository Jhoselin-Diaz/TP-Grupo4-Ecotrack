package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Service.SCocheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coches")
public class SCocheController {

    private final SCocheService sCocheService;
    public SCocheController(SCocheService SCocheService) {

        this.sCocheService = SCocheService;
    }

    @GetMapping("/listaAdmin")
    public List<SCocheDTO> listar() {

        return sCocheService.listaCocheAdmin();
    }

    @PostMapping("/registrar")
    public SCocheDTO registrarAlimento(@RequestBody SCocheCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return sCocheService.Registrar(dto, username);
    }

    @GetMapping("/MisCoches")
    public ResponseEntity<List<ListarCocheDTO>> listarMisCoches(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListarCocheDTO> lista = sCocheService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<SCocheDTO> actualizarCoche(
            @PathVariable Long id,
            @RequestBody SCocheCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        SCocheDTO result = sCocheService.actualizar(id, dto, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = sCocheService.calcularTotalEmisionesDelUsuarioC();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return sCocheService.eliminar(id);
    }
}
