package com.mycompany.projetoblade.controller;

import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.service.ClienteService;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável por coordenar as operações de clientes.
 * Aplicando SRP - responsável apenas por coordenar chamadas entre view e service.
 */
public class ClienteController {
    
    private final ClienteService clienteService;
    
    // Injeção de dependência via construtor
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    /**
     * Adiciona um novo cliente
     * @param cliente cliente a ser adicionado
     * @return cliente adicionado
     * @throws IllegalArgumentException se dados inválidos
     */
    public Cliente adicionarCliente(Cliente cliente) {
        return clienteService.salvarCliente(cliente);
    }
    
    /**
     * Lista todos os clientes
     * @return lista de todos os clientes
     */
    public List<Cliente> listarClientes() {
        return clienteService.listarTodos();
    }
    
    /**
     * Busca cliente por ID
     * @param id ID do cliente
     * @return Optional contendo o cliente se encontrado
     */
    public Optional<Cliente> buscarClientePorId(Integer id) {
        return clienteService.buscarPorId(id);
    }
    
    /**
     * Busca cliente por CPF
     * @param cpf CPF do cliente
     * @return Optional contendo o cliente se encontrado
     */
    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteService.buscarPorCpf(cpf);
    }
    
    /**
     * Busca clientes por nome
     * @param nome nome do cliente
     * @return lista de clientes com o nome
     */
    public List<Cliente> buscarClientesPorNome(String nome) {
        return clienteService.buscarPorNome(nome);
    }
    
    /**
     * Busca cliente por email
     * @param email email do cliente
     * @return Optional contendo o cliente se encontrado
     */
    public Optional<Cliente> buscarClientePorEmail(String email) {
        return clienteService.buscarPorEmail(email);
    }
    
    /**
     * Remove um cliente por ID
     * @param id ID do cliente
     * @return true se removido com sucesso
     */
    public boolean removerCliente(Integer id) {
        return clienteService.removerCliente(id);
    }
    
    /**
     * Atualiza um cliente existente
     * @param cliente cliente a ser atualizado
     * @return cliente atualizado
     * @throws IllegalArgumentException se dados inválidos
     */
    public Cliente atualizarCliente(Cliente cliente) {
        if (cliente.getId() <= 0) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para atualização");
        }
        
        // Verifica se o cliente existe
        Optional<Cliente> clienteExistente = clienteService.buscarPorId(cliente.getId());
        if (clienteExistente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        return clienteService.salvarCliente(cliente);
    }
    
    /**
     * Cria um novo cliente com usuário
     * @param nome nome do cliente
     * @param email email do cliente
     * @param senha senha do cliente
     * @param cpf CPF do cliente
     * @param endereco endereço do cliente
     * @return cliente criado
     * @throws IllegalArgumentException se dados inválidos
     */
    public Cliente criarCliente(String nome, String email, String senha, String cpf, String endereco) {
        Usuario usuario = new Usuario(nome, email, senha);
        Cliente cliente = new Cliente(endereco, cpf, usuario);
        return clienteService.salvarCliente(cliente);
    }
}
