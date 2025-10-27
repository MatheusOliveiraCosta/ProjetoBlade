package com.mycompany.projetoblade.controller;

import com.mycompany.projetoblade.model.Venda;
import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Pagamento;
import com.mycompany.projetoblade.service.VendaService;
import com.mycompany.projetoblade.service.VeiculoService;
import com.mycompany.projetoblade.service.ClienteService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável por coordenar as operações de vendas.
 * Aplicando SRP - responsável apenas por coordenar chamadas entre view e service.
 */
public class VendaController {
    
    private final VendaService vendaService;
    private final VeiculoService veiculoService;
    private final ClienteService clienteService;
    
    // Injeção de dependência via construtor
    public VendaController(VendaService vendaService, 
                          VeiculoService veiculoService, 
                          ClienteService clienteService) {
        this.vendaService = vendaService;
        this.veiculoService = veiculoService;
        this.clienteService = clienteService;
    }
    
    /**
     * Realiza uma nova venda
     * @param venda venda a ser realizada
     * @return venda realizada
     * @throws IllegalArgumentException se dados inválidos
     */
    public Venda realizarVenda(Venda venda) {
        return vendaService.realizarVenda(venda);
    }
    
    /**
     * Lista todas as vendas
     * @return lista de todas as vendas
     */
    public List<Venda> listarVendas() {
        return vendaService.listarTodas();
    }
    
    /**
     * Busca venda por ID
     * @param id ID da venda
     * @return Optional contendo a venda se encontrada
     */
    public Optional<Venda> buscarVendaPorId(Integer id) {
        return vendaService.buscarPorId(id);
    }
    
    /**
     * Busca vendas por cliente
     * @param clienteId ID do cliente
     * @return lista de vendas do cliente
     */
    public List<Venda> buscarVendasPorCliente(Integer clienteId) {
        return vendaService.buscarPorCliente(clienteId);
    }
    
    /**
     * Busca vendas por veículo
     * @param veiculoId ID do veículo
     * @return lista de vendas do veículo
     */
    public List<Venda> buscarVendasPorVeiculo(Integer veiculoId) {
        return vendaService.buscarPorVeiculo(veiculoId);
    }
    
    /**
     * Busca vendas por período
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return lista de vendas no período
     */
    public List<Venda> buscarVendasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return vendaService.buscarPorPeriodo(dataInicio, dataFim);
    }
    
    /**
     * Busca vendas por valor mínimo
     * @param valorMin valor mínimo
     * @return lista de vendas com valor maior ou igual
     */
    public List<Venda> buscarVendasPorValorMinimo(double valorMin) {
        return vendaService.buscarPorValorMinimo(valorMin);
    }
    
    /**
     * Calcula o total de vendas em um período
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return total de vendas no período
     */
    public double calcularTotalVendas(LocalDate dataInicio, LocalDate dataFim) {
        return vendaService.calcularTotalVendas(dataInicio, dataFim);
    }
    
    /**
     * Calcula a média de vendas em um período
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return média de vendas no período
     */
    public double calcularMediaVendas(LocalDate dataInicio, LocalDate dataFim) {
        return vendaService.calcularMediaVendas(dataInicio, dataFim);
    }
    
    /**
     * Cria uma nova venda com validações
     * @param clienteId ID do cliente
     * @param veiculoId ID do veículo
     * @param valorFinal valor final da venda
     * @param metodoPagamento método de pagamento
     * @return venda criada
     * @throws IllegalArgumentException se dados inválidos
     */
    public Venda criarVenda(Integer clienteId, Integer veiculoId, double valorFinal, String metodoPagamento) {
        // Busca cliente e veículo
        Optional<Cliente> cliente = clienteService.buscarPorId(clienteId);
        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        Optional<Veiculo> veiculo = veiculoService.buscarPorId(veiculoId);
        if (veiculo.isEmpty()) {
            throw new IllegalArgumentException("Veículo não encontrado");
        }
        
        // Cria pagamento
        Pagamento pagamento = new Pagamento(metodoPagamento, "PENDENTE");
        
        // Cria venda
        Venda venda = new Venda(LocalDate.now(), valorFinal, cliente.get(), veiculo.get());
        venda.setPagamento(pagamento);
        
        return vendaService.realizarVenda(venda);
    }
    
    /**
     * Lista vendas do dia atual
     * @return lista de vendas do dia
     */
    public List<Venda> listarVendasDoDia() {
        LocalDate hoje = LocalDate.now();
        return vendaService.buscarPorPeriodo(hoje, hoje);
    }
    
    /**
     * Lista vendas do mês atual
     * @return lista de vendas do mês
     */
    public List<Venda> listarVendasDoMes() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
        return vendaService.buscarPorPeriodo(inicioMes, fimMes);
    }
}
