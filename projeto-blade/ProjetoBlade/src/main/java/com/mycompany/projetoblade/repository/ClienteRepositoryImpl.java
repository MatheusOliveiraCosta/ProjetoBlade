package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Cliente;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação em memória do repositório de clientes.
 * Aplicando SRP - responsável apenas por operações de persistência de clientes.
 */
public class ClienteRepositoryImpl extends InMemoryRepository<Cliente, Integer> implements ClienteRepository {
    
    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        return findAll().stream()
                .filter(c -> c.getCpf() != null && c.getCpf().equals(cpf))
                .findFirst();
    }
    
    @Override
    public List<Cliente> findByNome(String nome) {
        return findAll().stream()
                .filter(c -> c.getUsuario() != null && 
                           c.getUsuario().getNome() != null && 
                           c.getUsuario().getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Cliente> findByEmail(String email) {
        return findAll().stream()
                .filter(c -> c.getUsuario() != null && 
                           c.getUsuario().getEmail() != null && 
                           c.getUsuario().getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
