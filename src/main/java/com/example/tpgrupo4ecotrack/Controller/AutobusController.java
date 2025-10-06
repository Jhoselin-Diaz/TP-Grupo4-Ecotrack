package com.example.tpgrupo4ecotrack.Controller;

import com.example.tpgrupo4ecotrack.DTO.AlimentoDTO;
import com.example.tpgrupo4ecotrack.DTO.AutobusDTO;
import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAlimento;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAutobus;
import com.example.tpgrupo4ecotrack.Service.AutobusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autobuses")
public class AutobusController {

    private final AutobusService autobusService;
    public AutobusController(AutobusService autobusService) {
        this.autobusService = autobusService;
    }


    @PostMapping("/inserta")
    public ResponseEntity<AutobusDTO> insertar(@RequestBody AutobusDTO dto) {
        return new ResponseEntity<>(autobusService.insertar(dto), HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Float> totalEmisiones() {
        Float total = autobusService.calcularTotalEmisionesDelUsuario();
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/elimina/{id}")
    public String eliminar(@PathVariable Long id) {
        return autobusService.eliminar(id);
    }
}
