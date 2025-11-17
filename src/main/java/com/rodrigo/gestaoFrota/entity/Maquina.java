package com.rodrigo.gestaoFrota.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "maquina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String tipo;

    private String modelo;

    private Integer ano;

    @Column(name = "numero_serie")
    private String numeroSerie;

    @Column(name = "horimetro_atual")
    private Double horimetroAtual;

    @Column(name = "horimetro_prox_revisao")
    private Double horimetroProxRevisao;

    @Column(name = "data_ultima_manutencao")
    private LocalDate dataUltimaManutencao;

    @Column(name = "data_prox_manutencao")
    private LocalDate dataProxManutencao;

    private Boolean ativo;
}
