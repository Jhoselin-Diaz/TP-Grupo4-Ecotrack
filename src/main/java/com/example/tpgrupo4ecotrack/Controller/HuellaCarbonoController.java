package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.DTO.SAlimentoDTO;
import com.example.tpgrupo4ecotrack.Entity.HuellaCarbono;
import com.example.tpgrupo4ecotrack.Service.HuellaCarbonoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

        return huellaCarbonoService.lista();
    }

    @PostMapping("/calcular/{idUsuario}")
    public ResponseEntity<HuellaDTO> calcularHuellaPorId(@PathVariable Long idUsuario) {
        HuellaDTO huella = huellaCarbonoService.calcularYGuardarHuellaPorId(idUsuario);
        return ResponseEntity.ok(huella);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {

        return huellaCarbonoService.eliminar(id);
    }
}