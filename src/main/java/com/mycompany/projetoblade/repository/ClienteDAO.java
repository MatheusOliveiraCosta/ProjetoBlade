package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO implements ClienteRepository {

    private static final String INSERT_USUARIO_SQL = "INSERT INTO USUARIO (nome, email, senha) VALUES (?, ?, ?)";
    private static final String INSERT_CLIENTE_SQL = "INSERT INTO CLIENTE (id_usuario, cpf, data_nascimento) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CLIENTES_SQL = "SELECT u.id, u.nome, u.email, u.senha, c.cpf, c.data_nascimento FROM USUARIO u JOIN CLIENTE c ON u.id = c.id_usuario";
    private static final String SELECT_CLIENTE_BY_ID_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE u.id = ?";
    private static final String SELECT_CLIENTE_BY_CPF_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE c.cpf = ?";
    private static final String SELECT_CLIENTE_BY_EMAIL_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE u.email = ?";

    @Override
    public Cliente save(Cliente cliente) {
        // Lógica de update não implementada para simplificar.
        // Uma implementação real verificaria se o ID existe para fazer UPDATE em vez de INSERT.
        if (cliente.getId() != 0) {
            System.err.println("Update de cliente não implementado.");
            return cliente;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Inserir na tabela USUARIO
            try (PreparedStatement psUsuario = conn.prepareStatement(INSERT_USUARIO_SQL, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, cliente.getUsuario().getNome());
                psUsuario.setString(2, cliente.getUsuario().getEmail());
                psUsuario.setString(3, cliente.getUsuario().getSenha());
                psUsuario.executeUpdate();

                try (ResultSet generatedKeys = psUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getInt(1)); // Pega o ID gerado
                    } else {
                        throw new SQLException("Falha ao criar usuário, nenhum ID obtido.");
                    }
                }
            }

            // 2. Inserir na tabela CLIENTE
            try (PreparedStatement psCliente = conn.prepareStatement(INSERT_CLIENTE_SQL)) {
                psCliente.setInt(1, cliente.getId());
                psCliente.setString(2, cliente.getCpf());
                psCliente.setDate(3, java.sql.Date.valueOf(cliente.getDataNascimento()));
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
                } catch (SQLException e) { /* Ignorar */ }
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
        // Implementação similar a findById, usando SELECT_CLIENTE_BY_CPF_SQL
        return Optional.empty();
    }

    @Override
    public List<Cliente> findByNome(String nome) {
        // Implementação requer um LIKE na query e um loop
        return new ArrayList<>();
    }

    @Override
    public Optional<Cliente> findByEmail(String email) {
        // Implementação similar a findById, usando SELECT_CLIENTE_BY_EMAIL_SQL
        return Optional.empty();
    }

    // Métodos restantes da interface Repository (deleteById, existsById, count) não implementados para brevidade.
    @Override public boolean deleteById(Integer id) { return false; }
    @Override public boolean existsById(Integer id) { return false; }
    @Override public long count() { return 0; }

    private Cliente mapRowToCliente(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setCpf(rs.getString("cpf"));
        Date dataNascimento = rs.getDate("data_nascimento");
        if (dataNascimento != null) {
            cliente.setDataNascimento(dataNascimento.toLocalDate());
        }
        return cliente;
    }
}