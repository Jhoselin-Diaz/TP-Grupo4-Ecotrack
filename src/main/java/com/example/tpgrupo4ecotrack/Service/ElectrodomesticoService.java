package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ElectrodomesticoDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.ElectrodomesticoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ElectrodomesticoService {

    private final ElectrodomesticoRepository electrodomesticoRepository;
    public ElectrodomesticoService(ElectrodomesticoRepository electrodomesticoRepository) {
        this.electrodomesticoRepository = electrodomesticoRepository;
    }


    public SubCategoriaElectrodomestico insertar(ElectrodomesticoDTO dto) {
        log.info("Insertando electrodoméstico {}", dto.getTipoElectrodomestico());
        ModelMapper modelMapper = new ModelMapper();
        SubCategoriaElectrodomestico electrodomestico = modelMapper.map(dto, SubCategoriaElectrodomestico.class);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getUsuario());
        electrodomestico.setUsuario(usuario);
        electrodomestico = electrodomesticoRepository.save(electrodomestico);

        return modelMapper.map(electrodomestico, SubCategoriaElectrodomestico.class);
    }

    public Long getTotalEmisionesElectrodomestico(Long usuarioId) {
        return electrodomesticoRepository.getTotalEmisionesByUsuario(usuarioId);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando electrodoméstico con ID: {}", id);
        electrodomesticoRepository.deleteById(id);
        return "Registro eliminado";
    }
}
