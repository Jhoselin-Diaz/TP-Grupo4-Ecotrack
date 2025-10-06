package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.Entity.HuellaCarbono;
import com.example.tpgrupo4ecotrack.Entity.ResultadoDetalle;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.HuellaCarbonoRepository;
import com.example.tpgrupo4ecotrack.Repository.ResultadoDetalleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HuellaCarbonoService {

    private final HuellaCarbonoRepository huellaCarbonoRepository;
    private final ResultadoDetalleRepository resultadoDetalleRepository;

    public HuellaCarbonoService(HuellaCarbonoRepository huellaCarbonoRepository, ResultadoDetalleRepository resultadoDetalleRepository) {
        this.huellaCarbonoRepository = huellaCarbonoRepository;
        this.resultadoDetalleRepository = resultadoDetalleRepository;
    }

    public List<HuellaDTO> obtenerHuellas() {
        log.info("Obteniendo lista de huellas de carbono");
        List<HuellaCarbono> lista = huellaCarbonoRepository.findAll();
        List<HuellaDTO> dtoList = new ArrayList<>();
        for (HuellaCarbono h : lista) {
            HuellaDTO dto = new HuellaDTO();
            dto.setIdHuella(h.getIdHuella());
            dto.setPeriodo(h.getPeriodo());
            dto.setFechaCalculo(h.getFechaCalculo());
            //dto.setTotalKgCO2((long) h.getTotalKgCO2());
            if (h.getUsuario() != null) dto.setUsuarioId(h.getUsuario().getIdUsuario());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public HuellaDTO insertar(HuellaDTO dto) {
        log.info("Insertando nueva huella de carbono");
        ModelMapper modelMapper = new ModelMapper();
        HuellaCarbono huellaCarbono = modelMapper.map(dto, HuellaCarbono.class);
        HuellaCarbono h = new HuellaCarbono();
        h.setPeriodo(dto.getPeriodo());
        h.setFechaCalculo(LocalDateTime.now());
        h.setTotalKgCO2((long) dto.getTotalKgCO2().floatValue());
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getUsuarioId());
        huellaCarbono.setUsuario(usuario);
        huellaCarbono = huellaCarbonoRepository.save(huellaCarbono);
        return modelMapper.map(huellaCarbono, HuellaDTO.class);



    }

    public List<HuellaDTO> EncontrarEntreAnio(Long albumId, LocalDate anioInicio, LocalDate anioFin) {
        return huellaCarbonoRepository.EncontrarEntreAnio(albumId, anioInicio, anioFin);
    }


    public String eliminar(Long id) {
        log.warn("Eliminando huella de carbono con ID: {}", id);
        huellaCarbonoRepository.deleteById(id);
        return "Registro eliminado";
    }
}