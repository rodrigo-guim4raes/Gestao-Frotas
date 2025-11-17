package com.rodrigo.gestaoFrota.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "registro_horas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroHoras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    private LocalDate data;

    @Column(name = "horas_trabalhadas")
    private Double horasTrabalhadas;

    @Column(columnDefinition = "TEXT")
    private String observacao;
}

