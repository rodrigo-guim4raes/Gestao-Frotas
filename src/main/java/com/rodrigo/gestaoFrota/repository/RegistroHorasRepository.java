package com.rodrigo.gestaoFrota.repository;

import com.rodrigo.gestaoFrota.entity.RegistroHoras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroHorasRepository extends JpaRepository<RegistroHoras, Long> {
    List<RegistroHoras> findByMaquinaIdOrderByDataAsc(Long maquinaId);
    List<RegistroHoras> findByMaquinaIdAndDataBetweenOrderByDataAsc(Long maquinaId,
                                                                    LocalDate dataInicio,
                                                                    LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(r.horasTrabalhadas), 0) " +
            "FROM RegistroHoras r " +
            "WHERE r.maquina.id = :maquinaId " +
            "AND r.data BETWEEN :dataInicio AND :dataFim")
    Double totalHorasTrabalhadasNoPeriodo(Long maquinaId, LocalDate dataInicio, LocalDate dataFim);
}

