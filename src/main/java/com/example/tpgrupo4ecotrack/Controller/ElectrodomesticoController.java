package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.AutobusDTO;
import com.example.tpgrupo4ecotrack.DTO.ElectrodomesticoDTO;
import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAutobus;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaElectrodomestico;
import com.example.tpgrupo4ecotrack.Service.ElectrodomesticoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/electrodomesticos")
public class ElectrodomesticoController {

    private final ElectrodomesticoService electrodomesticoService;
    public ElectrodomesticoController(ElectrodomesticoService electrodomesticoService) {
        this.electrodomesticoService = electrodomesticoService;
    }

    @PostMapping("/inserta")
    public ResponseEntity<SubCategoriaElectrodomestico> insertar(@RequestBody ElectrodomesticoDTO dto) {
        return new ResponseEntity<>(electrodomesticoService.insertar(dto), HttpStatus.OK);
    }

    @GetMapping("/total/{usuarioId}")
    public ResponseEntity<HuellaDTO> getTotalEmisionesElectrodomestico(@PathVariable Long usuarioId) {
        HuellaDTO huellaDTO = new HuellaDTO();
        huellaDTO.setTotalKgCO2(electrodomesticoService.getTotalEmisionesElectrodomestico(usuarioId));
        return new ResponseEntity<>(huellaDTO, HttpStatus.OK);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return electrodomesticoService.eliminar(id);
    }
}
