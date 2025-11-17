package com.rodrigo.gestaoFrota.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "abastecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    private LocalDate data;

    private Double litros;

    @Column(name = "custo_total")
    private Double custoTotal;

    // Horímetro no momento do abastecimento (para cálculo de consumo)
    private Double horimetro;
}

