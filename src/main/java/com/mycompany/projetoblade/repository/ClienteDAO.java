package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO implements ClienteRepository {

    // A tabela USUARIO não tem AUTO_INCREMENT no schema fornecido
    // Vamos gerar o ID manualmente consultando o próximo ID disponível
    private static final String GET_MAX_ID_SQL = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM USUARIO";
    private static final String INSERT_USUARIO_SQL = "INSERT INTO USUARIO (id, nome, email, senha) VALUES (?, ?, ?, ?)";
    // A tabela CLIENTE: id é PK e FK para USUARIO(id) - não tem coluna id_usuario separada
    private static final String INSERT_CLIENTE_SQL = "INSERT INTO CLIENTE (id, cpf, endereco) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CLIENTES_SQL = "SELECT u.id, u.nome, u.email, u.senha, c.cpf, c.endereco FROM USUARIO u JOIN CLIENTE c ON u.id = c.id";
    private static final String SELECT_CLIENTE_BY_ID_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE u.id = ?";
    private static final String SELECT_CLIENTE_BY_CPF_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE c.cpf = ?";
    private static final String SELECT_CLIENTE_BY_EMAIL_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE u.email = ?";
    private static final String SELECT_CLIENTE_BY_NOME_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE u.nome LIKE ?";

    @Override
    public Cliente save(Cliente cliente) {
        // Lógica de update não implementada para simplificar.
        // Uma implementação real verificaria se o ID existe para fazer UPDATE em vez de
        // INSERT.
        System.out.println("Salvando cliente no banco de dados: " + cliente);
        if (cliente.getId() != 0) {
            System.err.println("Update de cliente não implementado.");
            return cliente;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Obter o próximo ID disponível (já que USUARIO.id não tem AUTO_INCREMENT)
            int nextId;
            try (PreparedStatement psMaxId = conn.prepareStatement(GET_MAX_ID_SQL);
                 ResultSet rs = psMaxId.executeQuery()) {
                if (rs.next()) {
                    nextId = rs.getInt("next_id");
                } else {
                    nextId = 1; // Primeiro registro
                }
            }
            
            // 2. Inserir na tabela USUARIO com o ID gerado
            try (PreparedStatement psUsuario = conn.prepareStatement(INSERT_USUARIO_SQL)) {
                psUsuario.setInt(1, nextId);
                psUsuario.setString(2, cliente.getUsuario().getNome());
                psUsuario.setString(3, cliente.getUsuario().getEmail());
                psUsuario.setString(4, cliente.getUsuario().getSenha());
                psUsuario.executeUpdate();
                
                // Atualizar o ID no objeto cliente
                cliente.setId(nextId);
                cliente.getUsuario().setId(nextId);
            }

            // 3. Inserir na tabela CLIENTE
            // O id é PK e FK para USUARIO(id) - usa o mesmo ID do usuário
            try (PreparedStatement psCliente = conn.prepareStatement(INSERT_CLIENTE_SQL)) {
                psCliente.setInt(1, cliente.getId()); // id (mesmo ID do usuário, também é FK)
                psCliente.setString(2, cliente.getCpf());
                psCliente.setString(3, cliente.getEndereco());
                psCliente.executeUpdate();
            }

            conn.commit(); // Confirma a transação
            return cliente;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar cliente: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter transação: " + ex.getMessage());
                }
            }
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    /* Ignorar */ }
                DatabaseConnection.closeConnection(conn);
            }
        }
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CLIENTE_BY_ID_SQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CLIENTES_SQL);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os clientes: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CLIENTE_BY_CPF_SQL)) {
            pstmt.setString(1, cpf);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por CPF: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> findByNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CLIENTE_BY_NOME_SQL)) {
            pstmt.setString(1, "%" + nome + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                clientes.add(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar clientes por nome: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public Optional<Cliente> findByEmail(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CLIENTE_BY_EMAIL_SQL)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por email: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Métodos restantes da interface Repository (deleteById, existsById, count) não
    // implementados para brevidade.
    @Override
    public boolean deleteById(Integer id) {
        return false;
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    private Cliente mapRowToCliente(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));

        Cliente cliente = new Cliente();
        cliente.setId(usuario.getId()); // O ID do cliente é o mesmo do usuário
        cliente.setUsuario(usuario);
        cliente.setCpf(rs.getString("cpf"));
        cliente.setEndereco(rs.getString("endereco"));
        
        // Data de nascimento não está na tabela, deixar como null
        cliente.setDataNascimento(null);
        
        return cliente;
    }
}