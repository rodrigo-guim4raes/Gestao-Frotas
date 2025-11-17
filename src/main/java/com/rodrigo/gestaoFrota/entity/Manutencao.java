package com.rodrigo.gestaoFrota.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "manutencao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    private LocalDate data;

    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Double custo;

    @Column(name = "horas_uso_no_momento")
    private Double horasUsoNoMomento;
}

