package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAutobus;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaCoche;
import com.example.tpgrupo4ecotrack.Service.CocheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coches")
public class CocheController {

    private final CocheService cocheService;
    public CocheController(CocheService cocheService) {
        this.cocheService = cocheService;
    }

    @PostMapping("/inserta")
    public ResponseEntity<CocheDTO> insertar(@RequestBody CocheDTO dto) {
        return new ResponseEntity<>(cocheService.insertar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/MisCoche")
    public ResponseEntity<List<ListarCocheDTO>> listarMisAlimentos(Authentication authentication) {
        String username = authentication.getName(); // extrae del token JWT
        List<ListarCocheDTO> lista = cocheService.listarPorUsuario(username);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/total/{usuarioId}")
    public ResponseEntity<HuellaDTO> getTotalEmisionesCoche(@PathVariable Long usuarioId) {
        HuellaDTO huellaDTO = new HuellaDTO();
        huellaDTO.setTotalKgCO2(cocheService.getTotalEmisionesCoche(usuarioId));
        return new ResponseEntity<>(huellaDTO, HttpStatus.OK);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return cocheService.eliminar(id);
    }
}
