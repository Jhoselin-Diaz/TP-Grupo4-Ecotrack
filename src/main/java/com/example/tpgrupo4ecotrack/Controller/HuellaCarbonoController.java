package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.Entity.HuellaCarbono;
import com.example.tpgrupo4ecotrack.Service.HuellaCarbonoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/huellas")
public class HuellaCarbonoController {

    private final HuellaCarbonoService huellaCarbonoService;
    public HuellaCarbonoController(HuellaCarbonoService huellaCarbonoService) {
        this.huellaCarbonoService = huellaCarbonoService;
    }

    @GetMapping("/lista")
    public List<HuellaDTO> listar() {
        return huellaCarbonoService.obtenerHuellas();
    }

    @PostMapping("/inserta")
    public ResponseEntity<HuellaDTO> insertar(@RequestBody HuellaDTO huellaDTO) {
        return new ResponseEntity<>(huellaCarbonoService.insertar(huellaDTO), HttpStatus.CREATED);
    }

    @GetMapping("/encontrar/{usuarioID}/{anioInicio}/{anioFin}")
    public ResponseEntity<List<HuellaDTO>> consultarPorRango(
            @PathVariable Long usuarioID,
            @PathVariable LocalDate anioInicio,
            @PathVariable LocalDate anioFin) {
        return new ResponseEntity<>(huellaCarbonoService.EncontrarEntreAnio(usuarioID, anioInicio, anioFin), HttpStatus.OK);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return huellaCarbonoService.eliminar(id);
    }
}