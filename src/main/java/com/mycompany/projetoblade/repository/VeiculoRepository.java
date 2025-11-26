package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Veiculo;
import java.util.List;
import java.util.Optional;

/**
 * Interface específica para repositório de veículos.
 * Aplicando SRP - responsável apenas por operações específicas de veículos.
 */
public interface VeiculoRepository extends Repository<Veiculo, Integer> {
    
    /**
     * Busca veículos por marca
     * @param marca marca do veículo
     * @return lista de veículos da marca
     */
    List<Veiculo> findByMarca(String marca);
    
    /**
     * Busca veículos por modelo
     * @param modelo modelo do veículo
     * @return lista de veículos do modelo
     */
    List<Veiculo> findByModelo(String modelo);
    
    /**
     * Busca veículos por ano
     * @param ano ano do veículo
     * @return lista de veículos do ano
     */
    List<Veiculo> findByAno(int ano);
    
    /**
     * Busca veículos por status
     * @param status status do veículo
     * @return lista de veículos com o status
     */
    List<Veiculo> findByStatus(String status);
    
    /**
     * Busca veículo por placa
     * @param placa placa do veículo
     * @return Optional contendo o veículo se encontrado
     */
    Optional<Veiculo> findByPlaca(String placa);
    
    /**
     * Busca veículos por faixa de preço
     * @param precoMin preço mínimo
     * @param precoMax preço máximo
     * @return lista de veículos na faixa de preço
     */
    List<Veiculo> findByPrecoBetween(double precoMin, double precoMax);

    List<Veiculo> findByDono(int clienteId);
}
