package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Veiculo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação em memória do repositório de veículos.
 * Aplicando SRP - responsável apenas por operações de persistência de veículos.
 */
public class VeiculoRepositoryImpl extends InMemoryRepository<Veiculo, Integer> implements VeiculoRepository {
    
    @Override
    public List<Veiculo> findByMarca(String marca) {
        return findAll().stream()
                .filter(v -> v.getMarca() != null && v.getMarca().equalsIgnoreCase(marca))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Veiculo> findByModelo(String modelo) {
        return findAll().stream()
                .filter(v -> v.getModelo() != null && v.getModelo().equalsIgnoreCase(modelo))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Veiculo> findByAno(int ano) {
        return findAll().stream()
                .filter(v -> v.getAno() == ano)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Veiculo> findByStatus(String status) {
        return findAll().stream()
                .filter(v -> v.getStatus() != null && v.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Veiculo> findByPlaca(String placa) {
        return findAll().stream()
                .filter(v -> v.getPlaca() != null && v.getPlaca().equalsIgnoreCase(placa))
                .findFirst();
    }
    
    @Override
    public List<Veiculo> findByPrecoBetween(double precoMin, double precoMax) {
        return findAll().stream()
                .filter(v -> v.getPreco() >= precoMin && v.getPreco() <= precoMax)
                .collect(Collectors.toList());
    }
}
