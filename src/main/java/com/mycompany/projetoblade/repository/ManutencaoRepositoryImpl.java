package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Manutencao;
import java.util.List;
import java.util.stream.Collectors;

public class ManutencaoRepositoryImpl extends InMemoryRepository<Manutencao, Integer> implements ManutencaoRepository {

    @Override
    public List<Manutencao> findByVeiculoPlaca(String placa) {
        return findAll().stream()
                .filter(m -> m.getVeiculo() != null && m.getVeiculo().getPlaca() != null && m.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .collect(Collectors.toList());
    }

    @Override
    public List<Manutencao> findByStatus(String status) {
        return findAll().stream()
                .filter(m -> m.getStatus() != null && m.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}
