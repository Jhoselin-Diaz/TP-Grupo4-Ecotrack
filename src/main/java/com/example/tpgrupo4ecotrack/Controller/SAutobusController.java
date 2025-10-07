package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.SAutobusDTO;
import com.example.tpgrupo4ecotrack.Service.SAutobusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autobuses")
public class SAutobusController {

    private final SAutobusService SAutobusService;
    public SAutobusController(SAutobusService SAutobusService) {
        this.SAutobusService = SAutobusService;
    }


    @PostMapping("/inserta")
    public ResponseEntity<SAutobusDTO> insertar(@RequestBody SAutobusDTO dto) {
        return new ResponseEntity<>(SAutobusService.insertar(dto), HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = SAutobusService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return SAutobusService.eliminar(id);
    }
}
