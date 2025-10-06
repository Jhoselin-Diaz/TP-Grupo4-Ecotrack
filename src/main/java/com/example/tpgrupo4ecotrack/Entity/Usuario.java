package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Entity
@Data
@Table (name = "Usuario")
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String correo;

    @Column(nullable = false, length = 200)
    private String password;

    private Boolean enabled = true;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SubCategoriaAlimento> alimentos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SubCategoriaElectrodomestico> electrodomestico;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SubCategoriaRopa> ropas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SubCategoriaCoche> coches;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SubCategoriaAutobus> autobuses;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SubCategoriaServicioVivienda> serviciosViviendas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<HuellaCarbono> huellas;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private List<Role> roles;
}
