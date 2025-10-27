package com.mycompany.projetoblade.service;

import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.repository.VeiculoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio de veículos.
 * Aplicando SRP - responsável apenas pelas regras de negócio de veículos.
 */
public class VeiculoService {
    
    private final VeiculoRepository veiculoRepository;
    
    // Injeção de dependência via construtor
    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }
    
    /**
     * Salva um veículo aplicando validações de negócio
     * @param veiculo veículo a ser salvo
     * @return veículo salvo
     * @throws IllegalArgumentException se dados inválidos
     */
    public Veiculo salvarVeiculo(Veiculo veiculo) {
        validarVeiculo(veiculo);
        return veiculoRepository.save(veiculo);
    }
    
    /**
     * Busca veículo por ID
     * @param id ID do veículo
     * @return Optional contendo o veículo se encontrado
     */
    public Optional<Veiculo> buscarPorId(Integer id) {
        return veiculoRepository.findById(id);
    }
    
    /**
     * Lista todos os veículos
     * @return lista de todos os veículos
     */
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }
    
    /**
     * Busca veículos por marca
     * @param marca marca do veículo
     * @return lista de veículos da marca
     */
    public List<Veiculo> buscarPorMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("Marca não pode ser vazia");
        }
        return veiculoRepository.findByMarca(marca.trim());
    }
    
    /**
     * Busca veículos por modelo
     * @param modelo modelo do veículo
     * @return lista de veículos do modelo
     */
    public List<Veiculo> buscarPorModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo não pode ser vazio");
        }
        return veiculoRepository.findByModelo(modelo.trim());
    }
    
    /**
     * Busca veículos por ano
     * @param ano ano do veículo
     * @return lista de veículos do ano
     */
    public List<Veiculo> buscarPorAno(int ano) {
        if (ano < 1900 || ano > 2030) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2030");
        }
        return veiculoRepository.findByAno(ano);
    }
    
    /**
     * Busca veículos por status
     * @param status status do veículo
     * @return lista de veículos com o status
     */
    public List<Veiculo> buscarPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode ser vazio");
        }
        return veiculoRepository.findByStatus(status.trim());
    }
    
    /**
     * Busca veículo por placa
     * @param placa placa do veículo
     * @return Optional contendo o veículo se encontrado
     */
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Placa não pode ser vazia");
        }
        return veiculoRepository.findByPlaca(placa.trim().toUpperCase());
    }
    
    /**
     * Busca veículos por faixa de preço
     * @param precoMin preço mínimo
     * @param precoMax preço máximo
     * @return lista de veículos na faixa de preço
     */
    public List<Veiculo> buscarPorFaixaPreco(double precoMin, double precoMax) {
        if (precoMin < 0 || precoMax < 0) {
            throw new IllegalArgumentException("Preços não podem ser negativos");
        }
        if (precoMin > precoMax) {
            throw new IllegalArgumentException("Preço mínimo não pode ser maior que o máximo");
        }
        return veiculoRepository.findByPrecoBetween(precoMin, precoMax);
    }
    
    /**
     * Remove um veículo por ID
     * @param id ID do veículo
     * @return true se removido com sucesso
     */
    public boolean removerVeiculo(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        return veiculoRepository.deleteById(id);
    }
    
    /**
     * Valida os dados de um veículo
     * @param veiculo veículo a ser validado
     * @throws IllegalArgumentException se dados inválidos
     */
    private void validarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }
        
        if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("Marca é obrigatória");
        }
        
        if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo é obrigatório");
        }
        
        if (veiculo.getAno() < 1900 || veiculo.getAno() > 2030) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2030");
        }
        
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa é obrigatória");
        }
        
        if (veiculo.getChassi() == null || veiculo.getChassi().trim().isEmpty()) {
            throw new IllegalArgumentException("Chassi é obrigatório");
        }
        
        if (veiculo.getPreco() < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        
        // Verifica se já existe veículo com a mesma placa
        Optional<Veiculo> veiculoExistente = veiculoRepository.findByPlaca(veiculo.getPlaca());
        if (veiculoExistente.isPresent() && veiculoExistente.get().getIdVeiculo() != veiculo.getIdVeiculo()) {
            throw new IllegalArgumentException("Já existe um veículo com esta placa");
        }
    }
}
