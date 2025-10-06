package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ListaUsuarioDTO;
import com.example.tpgrupo4ecotrack.DTO.UsuarioDTO;
import com.example.tpgrupo4ecotrack.Entity.Role;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.RoleRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    public List<ListaUsuarioDTO> obtenerUsuarios() {
        log.info("Obteniendo lista de usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<ListaUsuarioDTO> usuarioDTOs = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            ListaUsuarioDTO dto = new ListaUsuarioDTO();
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setUsername(usuario.getUsername());
            dto.setPassword(usuario.getPassword());
            dto.setCorreo(usuario.getCorreo());
            dto.setEnabled(usuario.getEnabled());
            dto.setRol(usuario.getRoles().toString());
            usuarioDTOs.add(dto);
        }
        return usuarioDTOs;
    }

    public UsuarioDTO insertar(UsuarioDTO usuarioDTO) {
        log.info("Insertando nuevo usuario: {}", usuarioDTO.getUsername());
        ModelMapper modelMapper = new ModelMapper();
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuario = usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioDTO.getCorreo() != null && !usuarioDTO.getCorreo().isBlank())
            usuario.setCorreo(usuarioDTO.getCorreo());
        if (usuarioDTO.getUsername() != null && !usuarioDTO.getUsername().isBlank())
            usuario.setUsername(usuarioDTO.getUsername());
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isBlank())
            usuario.setPassword(usuarioDTO.getPassword());

        usuarioRepository.save(usuario);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(usuario, UsuarioDTO.class);
    }


    public String eliminar(Long id) {
        log.warn("Eliminando usuario con ID: {}", id);
        usuarioRepository.deleteById(id);
        return "Registro eliminado";
    }
}
