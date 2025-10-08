package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Service.SAutobusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autobuses")
public class SAutobusController {

    private final SAutobusService sAutobusService;
    public SAutobusController(SAutobusService sAutobusService) {

        this.sAutobusService = sAutobusService;
    }

    @GetMapping("/listaAdmin")
    public List<SAutobusDTO> listar() {

        return sAutobusService.listaAutobusAdmin();
    }

    @PostMapping("/registrar")
    public SAutobusDTO registrarAlimento(@RequestBody SAutobusCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return sAutobusService.Registrar(dto, username);
    }

    @GetMapping("/lista/MisAutobuses")
    public ResponseEntity<List<ListarAutobusDTO>> listarMisAutobuses(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListarAutobusDTO> lista = sAutobusService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<SAutobusDTO> actualizarCoche(
            @PathVariable Long id,
            @RequestBody SAutobusCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        SAutobusDTO result = sAutobusService.actualizar(id, dto, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = sAutobusService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return sAutobusService.eliminar(id);
    }
}
