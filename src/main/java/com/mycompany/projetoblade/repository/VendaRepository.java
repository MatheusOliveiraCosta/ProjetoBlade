package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Venda;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface específica para repositório de vendas.
 * Aplicando SRP - responsável apenas por operações específicas de vendas.
 */
public interface VendaRepository extends Repository<Venda, Integer> {
    
    /**
     * Busca vendas por cliente
     * @param clienteId ID do cliente
     * @return lista de vendas do cliente
     */
    List<Venda> findByClienteId(Integer clienteId);
    
    /**
     * Busca vendas por veículo
     * @param veiculoId ID do veículo
     * @return lista de vendas do veículo
     */
    List<Venda> findByVeiculoId(Integer veiculoId);
    
    /**
     * Busca vendas por período
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return lista de vendas no período
     */
    List<Venda> findByDataVendaBetween(LocalDate dataInicio, LocalDate dataFim);
    
    /**
     * Busca vendas por valor mínimo
     * @param valorMin valor mínimo
     * @return lista de vendas com valor maior ou igual
     */
    List<Venda> findByValorFinalGreaterThanEqual(double valorMin);
}
