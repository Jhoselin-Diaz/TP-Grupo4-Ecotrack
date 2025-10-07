package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.SElectrodomesticoDTO;
import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaElectrodomestico;
import com.example.tpgrupo4ecotrack.Service.SElectrodomesticoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/electrodomesticos")
public class SElectrodomesticoController {

    private final SElectrodomesticoService SElectrodomesticoService;
    public SElectrodomesticoController(SElectrodomesticoService SElectrodomesticoService) {
        this.SElectrodomesticoService = SElectrodomesticoService;
    }

    @PostMapping("/inserta")
    public ResponseEntity<SubCategoriaElectrodomestico> insertar(@RequestBody SElectrodomesticoDTO dto) {
        return new ResponseEntity<>(SElectrodomesticoService.insertar(dto), HttpStatus.OK);
    }

    @GetMapping("/total/{usuarioId}")
    public ResponseEntity<HuellaDTO> getTotalEmisionesElectrodomestico(@PathVariable Long usuarioId) {
        HuellaDTO huellaDTO = new HuellaDTO();
        huellaDTO.setTotalKgCO2(SElectrodomesticoService.getTotalEmisionesElectrodomestico(usuarioId));
        return new ResponseEntity<>(huellaDTO, HttpStatus.OK);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return SElectrodomesticoService.eliminar(id);
    }
}
