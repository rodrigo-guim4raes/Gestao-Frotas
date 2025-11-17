package com.rodrigo.gestaoFrota.repository;

import com.rodrigo.gestaoFrota.entity.Abastecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long> {
    List<Abastecimento> findByMaquinaIdOrderByDataAsc(Long maquinaId);
    List<Abastecimento> findByMaquinaIdAndDataBetweenOrderByDataAsc(Long maquinaId,
                                                                    LocalDate dataInicio,
                                                                    LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(a.litros), 0) " +
            "FROM Abastecimento a " +
            "WHERE a.maquina.id = :maquinaId " +
            "AND a.data BETWEEN :dataInicio AND :dataFim")
    Double totalLitrosNoPeriodo(Long maquinaId, LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(a.custoTotal), 0) " +
            "FROM Abastecimento a " +
            "WHERE a.maquina.id = :maquinaId " +
            "AND a.data BETWEEN :dataInicio AND :dataFim")
    Double totalCustoNoPeriodo(Long maquinaId, LocalDate dataInicio, LocalDate dataFim);
}

