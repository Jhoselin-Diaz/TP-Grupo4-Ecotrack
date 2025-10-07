package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Service.SAlimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alimentos")
public class SAlimentoController {

    private final SAlimentoService alimentoService;

    public SAlimentoController(SAlimentoService alimentoService) {

        this.alimentoService = alimentoService;
    }

    @GetMapping("/listaAdmin")
    public List<SAlimentoDTO> listar() {

        return alimentoService.listaAlimentosAdmin();
    }

    @GetMapping("/MisAlimentos")
    public ResponseEntity<List<ListaAlimentoPorUsuarioDTO>> listarMisAlimentos(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListaAlimentoPorUsuarioDTO> lista = alimentoService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/registrar")
    public SAlimentoDTO registrarAlimento(@RequestBody SAlimentoCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return alimentoService.Registrar(dto, username);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<SAlimentoDTO> actualizarUsuario(@PathVariable Long id, @RequestBody SAlimentoDTO dto) {
        SAlimentoDTO actualizado = alimentoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/MiActualizar/{id}")
    public ResponseEntity<SAlimentoDTO> actualizarAlimento(
            @PathVariable Long id,
            @RequestBody SAlimentoCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        SAlimentoDTO result = alimentoService.actualizarPorUser(id, dto, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = alimentoService.calcularTotalEmisionesDelUsuarioA();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {

        return alimentoService.eliminar(id);
    }
}
