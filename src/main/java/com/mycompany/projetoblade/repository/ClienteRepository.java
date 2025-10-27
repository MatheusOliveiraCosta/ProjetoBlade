package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Cliente;
import java.util.List;
import java.util.Optional;

/**
 * Interface específica para repositório de clientes.
 * Aplicando SRP - responsável apenas por operações específicas de clientes.
 */
public interface ClienteRepository extends Repository<Cliente, Integer> {
    
    /**
     * Busca cliente por CPF
     * @param cpf CPF do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByCpf(String cpf);
    
    /**
     * Busca clientes por nome
     * @param nome nome do cliente
     * @return lista de clientes com o nome
     */
    List<Cliente> findByNome(String nome);
    
    /**
     * Busca clientes por email
     * @param email email do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByEmail(String email);
}
