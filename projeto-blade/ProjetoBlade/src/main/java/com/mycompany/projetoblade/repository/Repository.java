package com.mycompany.projetoblade.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface genérica para repositórios.
 * Aplicando SRP - responsável apenas por definir operações de persistência.
 */
public interface Repository<T, ID> {
    
    /**
     * Salva uma entidade no repositório
     * @param entity entidade a ser salva
     * @return entidade salva
     */
    T save(T entity);
    
    /**
     * Busca uma entidade por ID
     * @param id identificador da entidade
     * @return Optional contendo a entidade se encontrada
     */
    Optional<T> findById(ID id);
    
    /**
     * Lista todas as entidades
     * @return lista de todas as entidades
     */
    List<T> findAll();
    
    /**
     * Remove uma entidade por ID
     * @param id identificador da entidade
     * @return true se removida com sucesso
     */
    boolean deleteById(ID id);
    
    /**
     * Verifica se uma entidade existe por ID
     * @param id identificador da entidade
     * @return true se existe
     */
    boolean existsById(ID id);
    
    /**
     * Conta o número total de entidades
     * @return número total de entidades
     */
    long count();
}
