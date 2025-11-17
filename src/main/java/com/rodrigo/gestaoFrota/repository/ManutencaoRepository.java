package com.rodrigo.gestaoFrota.repository;

import com.rodrigo.gestaoFrota.entity.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
    @Query("SELECT COALESCE(SUM(m.custo), 0) FROM Manutencao m WHERE m.maquina.id = :maquinaId")
    Double totalGastoPorMaquina(Long maquinaId);
}

