package com.example.tpgrupo4ecotrack.Service;


import com.example.tpgrupo4ecotrack.DTO.CategoriaDTO;
import com.example.tpgrupo4ecotrack.DTO.UsuarioDTO;
import com.example.tpgrupo4ecotrack.Entity.Categoria;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDTO> obtenerCategorias() {
        log.info("Obteniendo lista de categorías");
        List<Categoria> categorias = categoriaRepository.findAll();
        List<CategoriaDTO> categoriaDTOs = new ArrayList<>();
        for (Categoria c : categorias) {
            CategoriaDTO dto = new CategoriaDTO();
            dto.setIdCategoria(c.getIdCategoria());
            dto.setNombreCategoria(c.getNombreCategoria());
            categoriaDTOs.add(dto);
        }
        return categoriaDTOs;
    }

    public CategoriaDTO insertar(CategoriaDTO categoriaDTO) {
        log.info("Insertando nueva Categoria: {}", categoriaDTO.getNombreCategoria());
        ModelMapper modelMapper = new ModelMapper();
        Categoria categoria = modelMapper.map(categoriaDTO, Categoria.class);
        categoria = categoriaRepository.save(categoria);
        return modelMapper.map(categoria, CategoriaDTO.class);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando categoría con ID: {}", id);
        categoriaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
