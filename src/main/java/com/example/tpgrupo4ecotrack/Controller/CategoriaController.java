package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.AutobusDTO;
import com.example.tpgrupo4ecotrack.DTO.CategoriaDTO;
import com.example.tpgrupo4ecotrack.DTO.UsuarioDTO;
import com.example.tpgrupo4ecotrack.Entity.Categoria;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAutobus;
import com.example.tpgrupo4ecotrack.Service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/lista")
    public List<CategoriaDTO> listar() {
        return categoriaService.obtenerCategorias();
    }

    @PostMapping("/inserta")
    public ResponseEntity<CategoriaDTO> insertar(@RequestBody CategoriaDTO dto) {
        return new ResponseEntity<>(categoriaService.insertar(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return categoriaService.eliminar(id);
    }
}