package com.example.tpgrupo4ecotrack.Controller;


import com.example.tpgrupo4ecotrack.DTO.ListaUsuarioDTO;
import com.example.tpgrupo4ecotrack.DTO.UsuarioDTO;
import com.example.tpgrupo4ecotrack.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lista")
    public List<ListaUsuarioDTO> listar() {

        return usuarioService.obtenerUsuarios();
    }

    @PostMapping("/inserta")
    public ResponseEntity<UsuarioDTO> insertar(@RequestBody UsuarioDTO dto) {
        return new ResponseEntity<>(usuarioService.insertar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO actualizado = usuarioService.actualizar(id, usuarioDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return usuarioService.eliminar(id);
    }


}

