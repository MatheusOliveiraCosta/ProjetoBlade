package com.mycompany.projetoblade.service;

import com.mycompany.projetoblade.model.Manutencao;
import com.mycompany.projetoblade.repository.ManutencaoRepository;
import java.util.List;

/** Serviço simples em memória para manutenções */
public class ManutencaoService {
    private final ManutencaoRepository repo;

    public ManutencaoService(ManutencaoRepository repo) {
        this.repo = repo;
    }

    public Manutencao salvarManutencao(Manutencao m) {
        if (m == null) throw new IllegalArgumentException("Manutenção não pode ser nula");
        if (m.getStatus() == null || m.getStatus().trim().isEmpty()) {
            m.setStatus("AGUARDANDO"); // regra: status inicial
        }
        return repo.save(m);
    }

    public List<Manutencao> listarTodos() {
        return repo.findAll();
    }

    public List<Manutencao> buscarPorPlaca(String placa) {
        return repo.findByVeiculoPlaca(placa);
    }

    public List<Manutencao> buscarPorStatus(String status) {
        return repo.findByStatus(status);
    }
}
