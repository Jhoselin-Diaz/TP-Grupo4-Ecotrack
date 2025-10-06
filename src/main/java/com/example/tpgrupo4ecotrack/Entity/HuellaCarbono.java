package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Contract;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "huellas_carbono")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuellaCarbono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHuella;

    private String periodo;

    private LocalDateTime fechaCalculo;

    private Long totalKgCO2;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "huella", cascade = CascadeType.ALL)
    private List<Resultado> resultados;
}