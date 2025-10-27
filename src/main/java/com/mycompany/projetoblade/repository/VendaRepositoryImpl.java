package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Venda;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação em memória do repositório de vendas.
 * Aplicando SRP - responsável apenas por operações de persistência de vendas.
 */
public class VendaRepositoryImpl extends InMemoryRepository<Venda, Integer> implements VendaRepository {
    
    @Override
    public List<Venda> findByClienteId(Integer clienteId) {
        return findAll().stream()
                .filter(v -> v.getCliente() != null && v.getCliente().getId() == clienteId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Venda> findByVeiculoId(Integer veiculoId) {
        return findAll().stream()
                .filter(v -> v.getVeiculo() != null && v.getVeiculo().getIdVeiculo() == veiculoId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Venda> findByDataVendaBetween(LocalDate dataInicio, LocalDate dataFim) {
        return findAll().stream()
                .filter(v -> v.getDataVenda() != null && 
                           !v.getDataVenda().isBefore(dataInicio) && 
                           !v.getDataVenda().isAfter(dataFim))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Venda> findByValorFinalGreaterThanEqual(double valorMin) {
        return findAll().stream()
                .filter(v -> v.getValorFinal() >= valorMin)
                .collect(Collectors.toList());
    }
}
