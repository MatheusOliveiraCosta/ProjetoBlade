package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Manutencao;
import java.util.List;

/**
 * Repositório específico para Manutenção.
 * Define consultas adicionais além das operações básicas definidas em Repository.
 */
public interface ManutencaoRepository extends Repository<Manutencao, Integer> {
    List<Manutencao> findByVeiculoPlaca(String placa);
    List<Manutencao> findByStatus(String status);
}
