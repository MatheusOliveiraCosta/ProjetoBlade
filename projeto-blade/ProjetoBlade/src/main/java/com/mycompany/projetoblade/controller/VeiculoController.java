package com.mycompany.projetoblade.controller;

import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.service.VeiculoService;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável por coordenar as operações de veículos.
 * Aplicando SRP - responsável apenas por coordenar chamadas entre view e service.
 */
public class VeiculoController {
    
    private final VeiculoService veiculoService;
    
    // Injeção de dependência via construtor
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }
    
    /**
     * Adiciona um novo veículo
     * @param veiculo veículo a ser adicionado
     * @return veículo adicionado
     * @throws IllegalArgumentException se dados inválidos
     */
    public Veiculo adicionarVeiculo(Veiculo veiculo) {
        return veiculoService.salvarVeiculo(veiculo);
    }
    
    /**
     * Lista todos os veículos
     * @return lista de todos os veículos
     */
    public List<Veiculo> listarVeiculos() {
        return veiculoService.listarTodos();
    }
    
    /**
     * Busca veículo por ID
     * @param id ID do veículo
     * @return Optional contendo o veículo se encontrado
     */
    public Optional<Veiculo> buscarVeiculoPorId(Integer id) {
        return veiculoService.buscarPorId(id);
    }
    
    /**
     * Busca veículos por marca
     * @param marca marca do veículo
     * @return lista de veículos da marca
     */
    public List<Veiculo> buscarVeiculosPorMarca(String marca) {
        return veiculoService.buscarPorMarca(marca);
    }
    
    /**
     * Busca veículos por modelo
     * @param modelo modelo do veículo
     * @return lista de veículos do modelo
     */
    public List<Veiculo> buscarVeiculosPorModelo(String modelo) {
        return veiculoService.buscarPorModelo(modelo);
    }
    
    /**
     * Busca veículos por ano
     * @param ano ano do veículo
     * @return lista de veículos do ano
     */
    public List<Veiculo> buscarVeiculosPorAno(int ano) {
        return veiculoService.buscarPorAno(ano);
    }
    
    /**
     * Busca veículos por status
     * @param status status do veículo
     * @return lista de veículos com o status
     */
    public List<Veiculo> buscarVeiculosPorStatus(String status) {
        return veiculoService.buscarPorStatus(status);
    }
    
    /**
     * Busca veículo por placa
     * @param placa placa do veículo
     * @return Optional contendo o veículo se encontrado
     */
    public Optional<Veiculo> buscarVeiculoPorPlaca(String placa) {
        return veiculoService.buscarPorPlaca(placa);
    }
    
    /**
     * Busca veículos por faixa de preço
     * @param precoMin preço mínimo
     * @param precoMax preço máximo
     * @return lista de veículos na faixa de preço
     */
    public List<Veiculo> buscarVeiculosPorFaixaPreco(double precoMin, double precoMax) {
        return veiculoService.buscarPorFaixaPreco(precoMin, precoMax);
    }
    
    /**
     * Remove um veículo por ID
     * @param id ID do veículo
     * @return true se removido com sucesso
     */
    public boolean removerVeiculo(Integer id) {
        return veiculoService.removerVeiculo(id);
    }
    
    /**
     * Atualiza um veículo existente
     * @param veiculo veículo a ser atualizado
     * @return veículo atualizado
     * @throws IllegalArgumentException se dados inválidos
     */
    public Veiculo atualizarVeiculo(Veiculo veiculo) {
        if (veiculo.getIdVeiculo() <= 0) {
            throw new IllegalArgumentException("ID do veículo é obrigatório para atualização");
        }
        
        // Verifica se o veículo existe
        Optional<Veiculo> veiculoExistente = veiculoService.buscarPorId(veiculo.getIdVeiculo());
        if (veiculoExistente.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }
        
        return veiculoService.salvarVeiculo(veiculo);
    }
    
    /**
     * Lista veículos disponíveis para venda
     * @return lista de veículos disponíveis
     */
    public List<Veiculo> listarVeiculosDisponiveis() {
        return veiculoService.buscarPorStatus("DISPONIVEL");
    }
    
    /**
     * Lista veículos vendidos
     * @return lista de veículos vendidos
     */
    public List<Veiculo> listarVeiculosVendidos() {
        return veiculoService.buscarPorStatus("VENDIDO");
    }
}
