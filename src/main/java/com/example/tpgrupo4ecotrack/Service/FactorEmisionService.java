package com.example.tpgrupo4ecotrack.Service;


import com.example.tpgrupo4ecotrack.DTO.FactorEmisionDTO;
import com.example.tpgrupo4ecotrack.DTO.UsuarioDTO;
import com.example.tpgrupo4ecotrack.Entity.FactorEmision;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FactorEmisionService {

    private final FactorEmisionRepository factorEmisionRepository;

    public FactorEmisionService(FactorEmisionRepository factorEmisionRepository) {
        this.factorEmisionRepository = factorEmisionRepository;
    }

    public List<FactorEmisionDTO> obtenerFactores() {
        log.info("Obteniendo lista de factores de emisión");
        List<FactorEmision> factores = factorEmisionRepository.findAll();
        List<FactorEmisionDTO> factorDTOs = new ArrayList<>();
        for (FactorEmision f : factores) {
            FactorEmisionDTO dto = new FactorEmisionDTO();
            dto.setIdFactor(f.getIdFactor());
            dto.setCodigo(f.getCodigo());
            dto.setUnidad(f.getUnidad());
            dto.setFactorKgCO2PorUnidad((Float) f.getFactorKgCO2PorUnidad());
            factorDTOs.add(dto);
        }
        return factorDTOs;
    }

    public FactorEmisionDTO insertar(FactorEmisionDTO factorEmisionDTO) {
        log.info("Insertando nuevo Factor Emisión: {}", factorEmisionDTO.getCodigo());
        ModelMapper modelMapper = new ModelMapper();
        FactorEmision factorEmision = modelMapper.map(factorEmisionDTO, FactorEmision.class);
        factorEmision = factorEmisionRepository.save(factorEmision);
        return modelMapper.map(factorEmision, FactorEmisionDTO.class);
    }

    public FactorEmisionDTO actualizar(Long id, FactorEmisionDTO factorEmisionDTO) {
        FactorEmision factorEmision = factorEmisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factor Emisión no encontrado"));

        if (factorEmisionDTO.getCodigo() != null && !factorEmisionDTO.getCodigo().isBlank())
            factorEmision.setCodigo(factorEmisionDTO.getCodigo());
        if (factorEmisionDTO.getUnidad() != null && !factorEmisionDTO.getUnidad().isBlank())
            factorEmision.setUnidad(factorEmisionDTO.getUnidad());
        if (factorEmisionDTO.getFactorKgCO2PorUnidad() != null &&
                !Float.isNaN(factorEmisionDTO.getFactorKgCO2PorUnidad())) {
            factorEmision.setFactorKgCO2PorUnidad(factorEmisionDTO.getFactorKgCO2PorUnidad());
        }
        factorEmisionRepository.save(factorEmision);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(factorEmision, FactorEmisionDTO.class);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando factor de emisión con ID: {}", id);
        factorEmisionRepository.deleteById(id);
        return "Registro eliminado";
    }
}
