package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.SElectrodomesticoDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SElectrodomesticoRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SElectrodomesticoService {

    private final SElectrodomesticoRepository SelectrodomesticoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;

    public SElectrodomesticoService(SElectrodomesticoRepository SelectrodomesticoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository,  FactorEmisionRepository factorEmisionRepository) {
        this.SelectrodomesticoRepository = SelectrodomesticoRepository;
        this. usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }


    public SubCategoriaElectrodomestico insertar(SElectrodomesticoDTO dto) {
        log.info("Insertando electrodoméstico {}", dto.getTipoElectrodomestico());
        ModelMapper modelMapper = new ModelMapper();
        SubCategoriaElectrodomestico electrodomestico = modelMapper.map(dto, SubCategoriaElectrodomestico.class);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getUsuarioid());
        electrodomestico.setUsuario(usuario);
        electrodomestico = SelectrodomesticoRepository.save(electrodomestico);

        return modelMapper.map(electrodomestico, SubCategoriaElectrodomestico.class);
    }

    public Long getTotalEmisionesElectrodomestico(Long usuarioId) {
        return SelectrodomesticoRepository.getTotalEmisionesByUsuario(usuarioId);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando electrodoméstico con ID: {}", id);
        SelectrodomesticoRepository.deleteById(id);
        return "Registro eliminado";
    }
}
