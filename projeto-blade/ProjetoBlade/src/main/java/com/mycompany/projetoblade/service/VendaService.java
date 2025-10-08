package com.mycompany.projetoblade.service;

import com.mycompany.projetoblade.model.Venda;
import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Pagamento;
import com.mycompany.projetoblade.repository.VendaRepository;
import com.mycompany.projetoblade.repository.VeiculoRepository;
import com.mycompany.projetoblade.repository.ClienteRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio de vendas.
 * Aplicando SRP - responsável apenas pelas regras de negócio de vendas.
 */
public class VendaService {
    
    private final VendaRepository vendaRepository;
    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;
    
    // Injeção de dependência via construtor
    public VendaService(VendaRepository vendaRepository, 
                       VeiculoRepository veiculoRepository, 
                       ClienteRepository clienteRepository) {
        this.vendaRepository = vendaRepository;
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }
    
    /**
     * Realiza uma venda aplicando validações de negócio
     * @param venda venda a ser realizada
     * @return venda realizada
     * @throws IllegalArgumentException se dados inválidos
     */
    public Venda realizarVenda(Venda venda) {
        validarVenda(venda);
        
        // Atualiza status do veículo para vendido
        Veiculo veiculo = venda.getVeiculo();
        veiculo.setStatus("VENDIDO");
        veiculoRepository.save(veiculo);
        
        // Salva a venda
        return vendaRepository.save(venda);
    }
    
    /**
     * Busca venda por ID
     * @param id ID da venda
     * @return Optional contendo a venda se encontrada
     */
    public Optional<Venda> buscarPorId(Integer id) {
        return vendaRepository.findById(id);
    }
    
    /**
     * Lista todas as vendas
     * @return lista de todas as vendas
     */
    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }
    
    /**
     * Busca vendas por cliente
     * @param clienteId ID do cliente
     * @return lista de vendas do cliente
     */
    public List<Venda> buscarPorCliente(Integer clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ID do cliente deve ser um número positivo");
        }
        return vendaRepository.findByClienteId(clienteId);
    }
    
    /**
     * Busca vendas por veículo
     * @param veiculoId ID do veículo
     * @return lista de vendas do veículo
     */
    public List<Venda> buscarPorVeiculo(Integer veiculoId) {
        if (veiculoId == null || veiculoId <= 0) {
            throw new IllegalArgumentException("ID do veículo deve ser um número positivo");
        }
        return vendaRepository.findByVeiculoId(veiculoId);
    }
    
    /**
     * Busca vendas por período
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return lista de vendas no período
     */
    public List<Venda> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return vendaRepository.findByDataVendaBetween(dataInicio, dataFim);
    }
    
    /**
     * Busca vendas por valor mínimo
     * @param valorMin valor mínimo
     * @return lista de vendas com valor maior ou igual
     */
    public List<Venda> buscarPorValorMinimo(double valorMin) {
        if (valorMin < 0) {
            throw new IllegalArgumentException("Valor mínimo não pode ser negativo");
        }
        return vendaRepository.findByValorFinalGreaterThanEqual(valorMin);
    }
    
    /**
     * Calcula o total de vendas em um período
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return total de vendas no período
     */
    public double calcularTotalVendas(LocalDate dataInicio, LocalDate dataFim) {
        List<Venda> vendas = buscarPorPeriodo(dataInicio, dataFim);
        return vendas.stream()
                .mapToDouble(Venda::getValorFinal)
                .sum();
    }
    
    /**
     * Calcula a média de vendas em um período
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return média de vendas no período
     */
    public double calcularMediaVendas(LocalDate dataInicio, LocalDate dataFim) {
        List<Venda> vendas = buscarPorPeriodo(dataInicio, dataFim);
        if (vendas.isEmpty()) {
            return 0.0;
        }
        return vendas.stream()
                .mapToDouble(Venda::getValorFinal)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Valida os dados de uma venda
     * @param venda venda a ser validada
     * @throws IllegalArgumentException se dados inválidos
     */
    private void validarVenda(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("Venda não pode ser nula");
        }
        
        if (venda.getCliente() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        
        if (venda.getVeiculo() == null) {
            throw new IllegalArgumentException("Veículo é obrigatório");
        }
        
        if (venda.getDataVenda() == null) {
            throw new IllegalArgumentException("Data da venda é obrigatória");
        }
        
        if (venda.getDataVenda().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data da venda não pode ser futura");
        }
        
        if (venda.getValorFinal() <= 0) {
            throw new IllegalArgumentException("Valor final deve ser maior que zero");
        }
        
        // Verifica se o cliente existe
        Optional<Cliente> cliente = clienteRepository.findById(venda.getCliente().getId());
        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        // Verifica se o veículo existe e está disponível
        Optional<Veiculo> veiculo = veiculoRepository.findById(venda.getVeiculo().getIdVeiculo());
        if (veiculo.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }
        
        if (!"DISPONIVEL".equalsIgnoreCase(veiculo.get().getStatus())) {
            throw new IllegalArgumentException("Veículo não está disponível para venda");
        }
    }
}
