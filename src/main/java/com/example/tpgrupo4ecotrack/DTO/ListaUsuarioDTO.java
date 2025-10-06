package com.example.tpgrupo4ecotrack.DTO;


import com.example.tpgrupo4ecotrack.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaUsuarioDTO {
    private Long idUsuario;
    private String username;
    private String correo;
    private String password;
    private Boolean enabled;
    private String rol;
}
