package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaElectrodomestico;
import com.example.tpgrupo4ecotrack.Repository.SElectrodomesticoRepository;
import com.example.tpgrupo4ecotrack.Service.SElectrodomesticoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/electrodomesticos")
public class SElectrodomesticoController {

    private final SElectrodomesticoService sElectrodomesticoService;

    public SElectrodomesticoController(SElectrodomesticoService SElectrodomesticoService) {
        this.sElectrodomesticoService = SElectrodomesticoService;
    }

    @GetMapping("/listaAdmin")
    public List<SElectrodomesticoDTO> listar() {

        return sElectrodomesticoService.listaElectrodomesticoAdmin();
    }

    @PostMapping("/registrar")
    public SElectrodomesticoDTO registrarElectrodomestico(@RequestBody SElectCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return sElectrodomesticoService.Registrar(dto, username);
    }

    @GetMapping("/MisElectrodomestico")
    public ResponseEntity<List<ListarElectDTO>> listarMisElectrodomestico(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListarElectDTO> lista = sElectrodomesticoService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<SElectrodomesticoDTO> actualizarElectrodomestico(
            @PathVariable Long id,
            @RequestBody SElectCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        SElectrodomesticoDTO result = sElectrodomesticoService.actualizar(id, dto, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = sElectrodomesticoService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return sElectrodomesticoService.eliminar(id);
    }
}
