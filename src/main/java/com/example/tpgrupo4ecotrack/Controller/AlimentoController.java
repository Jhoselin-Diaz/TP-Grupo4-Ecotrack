package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAlimento;
import com.example.tpgrupo4ecotrack.Service.AlimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alimentos")
public class AlimentoController {

    private final AlimentoService alimentoService;

    public AlimentoController(AlimentoService alimentoService) {

        this.alimentoService = alimentoService;
    }

    @GetMapping("/lista")
    public List<AlimentoDTO> listar() {

        return alimentoService.listaAlimentos();
    }

    @GetMapping("/MisAlimentos")
    public ResponseEntity<List<ListaAlimentoPorUsuarioDTO>> listarMisAlimentos(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListaAlimentoPorUsuarioDTO> lista = alimentoService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/inserta")
    public ResponseEntity<AlimentoDTO> insertar(@RequestBody AlimentoDTO dto) {
        return new ResponseEntity<>(alimentoService.insertar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<AlimentoDTO> actualizarUsuario(@PathVariable Long id, @RequestBody AlimentoDTO dto) {
        AlimentoDTO actualizado = alimentoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/MiActualizar/{id}")
    public ResponseEntity<AlimentoDTO> actualizarAlimento(
            @PathVariable Long id,
            @RequestBody AlimentoCreateDTO dto,
            Authentication authentication) {

        String username = authentication.getName();
        AlimentoDTO result = alimentoService.actualizarPorUser(id, dto, username);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = alimentoService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {

        return alimentoService.eliminar(id);
    }
}
