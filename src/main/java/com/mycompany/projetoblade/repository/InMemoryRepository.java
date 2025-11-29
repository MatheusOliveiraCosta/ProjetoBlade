package com.mycompany.projetoblade.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementação em memória do repositório genérico.
 * Aplicando SRP - responsável apenas por armazenar e recuperar objetos em memória.
 */
public class InMemoryRepository<T, ID> implements Repository<T, ID> {
    
    private final Map<ID, T> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Override
    @SuppressWarnings("unchecked")
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        // Se a entidade tem um ID, usa ele; senão gera um novo
        try {
            // Tenta obter o ID da entidade usando reflexão
            // Primeiro tenta campos comuns de ID
            String[] possibleIdFields = {"id", "idVeiculo", "idVenda", "idCliente"};
            Object currentId = null;
            
            for (String fieldName : possibleIdFields) {
                try {
                    var idField = entity.getClass().getDeclaredField(fieldName);
                    idField.setAccessible(true);
                    currentId = idField.get(entity);
                    if (currentId != null) {
                        break;
                    }
                } catch (NoSuchFieldException ignored) {
                    // Continua tentando outros campos
                }
            }
            
            if (currentId == null || (currentId instanceof Number && ((Number) currentId).longValue() == 0)) {
                // Gera novo ID
                long newId = idGenerator.getAndIncrement();
                // Tenta definir o ID no primeiro campo encontrado
                for (String fieldName : possibleIdFields) {
                    try {
                        var idField = entity.getClass().getDeclaredField(fieldName);
                        idField.setAccessible(true);
                        idField.set(entity, (int) newId);
                        break;
                    } catch (NoSuchFieldException ignored) {
                        // Continua tentando outros campos
                    }
                }
                storage.put((ID) Integer.valueOf((int) newId), entity);
            } else {
                // Atualiza entidade existente
                storage.put((ID) currentId, entity);
            }
        } catch (Exception e) {
            // Se não conseguir obter o ID, gera um novo
            long newId = idGenerator.getAndIncrement();
            storage.put((ID) Integer.valueOf((int) newId), entity);
        }
        
        return entity;
    }
    
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public boolean deleteById(ID id) {
        return storage.remove(id) != null;
    }
    
    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }
    
    @Override
    public long count() {
        return storage.size();
    }
}
