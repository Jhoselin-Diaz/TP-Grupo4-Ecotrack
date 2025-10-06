package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.FactorEmisionDTO;
import com.example.tpgrupo4ecotrack.DTO.UsuarioDTO;
import com.example.tpgrupo4ecotrack.Entity.FactorEmision;
import com.example.tpgrupo4ecotrack.Service.FactorEmisionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factores")
public class FactorEmisionController {

    private final FactorEmisionService factorEmisionService;
    public FactorEmisionController(FactorEmisionService factorEmisionService) {
        this.factorEmisionService = factorEmisionService;
    }

    @GetMapping("/lista")
    public List<FactorEmisionDTO> listar() {
        return factorEmisionService.obtenerFactores();
    }

    @PostMapping("/inserta")
    public ResponseEntity<FactorEmisionDTO> insertar(@RequestBody FactorEmisionDTO dto) {
        return new ResponseEntity<>(factorEmisionService.insertar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<FactorEmisionDTO> actualizarUsuario(@PathVariable Long id, @RequestBody FactorEmisionDTO factorEmisionDTO) {
        FactorEmisionDTO actualizado = factorEmisionService.actualizar(id, factorEmisionDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return factorEmisionService.eliminar(id);
    }
}