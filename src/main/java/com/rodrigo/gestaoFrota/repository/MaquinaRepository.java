package com.rodrigo.gestaoFrota.repository;

import com.rodrigo.gestaoFrota.entity.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
    List<Maquina> findByHorimetroAtualGreaterThanEqual(Double valor);
    List<Maquina> findByDataProxManutencaoBefore(LocalDate data);
}

