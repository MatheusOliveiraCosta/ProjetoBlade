package com.mycompany.projetoblade.service;

import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.repository.ClienteRepository;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio de clientes.
 * Aplicando SRP - responsável apenas pelas regras de negócio de clientes.
 */
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    // Injeção de dependência via construtor
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    /**
     * Salva um cliente aplicando validações de negócio
     * @param cliente cliente a ser salvo
     * @return cliente salvo
     * @throws IllegalArgumentException se dados inválidos
     */
    public Cliente salvarCliente(Cliente cliente) {
        validarCliente(cliente);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        System.out.println("Cliente salvo com sucesso: " + clienteSalvo);
        return clienteSalvo;
    }
    
    /**
     * Busca cliente por ID
     * @param id ID do cliente
     * @return Optional contendo o cliente se encontrado
     */
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }
    
    /**
     * Lista todos os clientes
     * @return lista de todos os clientes
     */
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    /**
     * Busca cliente por CPF
     * @param cpf CPF do cliente
     * @return Optional contendo o cliente se encontrado
     */
    public Optional<Cliente> buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
        return clienteRepository.findByCpf(cpf.trim());
    }
    
    /**
     * Busca clientes por nome
     * @param nome nome do cliente
     * @return lista de clientes com o nome
     */
    public List<Cliente> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return clienteRepository.findByNome(nome.trim());
    }
    
    /**
     * Busca cliente por email
     * @param email email do cliente
     * @return Optional contendo o cliente se encontrado
     */
    public Optional<Cliente> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        return clienteRepository.findByEmail(email.trim());
    }
    
    /**
     * Remove um cliente por ID
     * @param id ID do cliente
     * @return true se removido com sucesso
     */
    public boolean removerCliente(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        return clienteRepository.deleteById(id);
    }
    
    /**
     * Valida os dados de um cliente
     * @param cliente cliente a ser validado
     * @throws IllegalArgumentException se dados inválidos
     */
    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        
        if (cliente.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }
        
        validarUsuario(cliente.getUsuario());
        
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (!validarCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        
        if (cliente.getEndereco() == null || cliente.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }
        
        // Verifica se já existe cliente com o mesmo CPF
        Optional<Cliente> clienteExistente = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteExistente.isPresent() && clienteExistente.get().getId() != cliente.getId()) {
            throw new IllegalArgumentException("Já existe um cliente com este CPF");
        }
        
        // Verifica se já existe cliente com o mesmo email
        Optional<Cliente> clienteComEmail = clienteRepository.findByEmail(cliente.getUsuario().getEmail());
        if (clienteComEmail.isPresent() && clienteComEmail.get().getId() != cliente.getId()) {
            throw new IllegalArgumentException("Já existe um cliente com este email");
        }
    }
    
    /**
     * Valida os dados de um usuário
     * @param usuario usuário a ser validado
     * @throws IllegalArgumentException se dados inválidos
     */
    private void validarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (!validarEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        
        if (usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
    }
    
    /**
     * Valida formato de email
     * @param email email a ser validado
     * @return true se válido
     */
    private boolean validarEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Valida formato de CPF
     * @param cpf CPF a ser validado
     * @return true se válido
     */
    private boolean validarCpf(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validação do CPF
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;
        
        if (Character.getNumericValue(cpf.charAt(9)) != digito1) {
            return false;
        }
        
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        resto = soma % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;
        
        return Character.getNumericValue(cpf.charAt(10)) == digito2;
    }

    public Cliente autenticar(String email, String senha) {
    // Busca o cliente pelo email
    Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
    
    if (clienteOpt.isPresent()) {
        Cliente cliente = clienteOpt.get();
        // Verifica se a senha bate (na vida real usaria hash, aqui comparamos texto)
        if (cliente.getUsuario().getSenha().equals(senha)) {
            return cliente; // Login sucesso!
        }
    }
    return null; // Falha no login
    }
}
