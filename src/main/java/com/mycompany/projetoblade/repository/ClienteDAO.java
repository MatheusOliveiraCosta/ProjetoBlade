package com.mycompany.projetoblade.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.utils.DatabaseConnection;

public class ClienteDAO implements ClienteRepository {

    private static final String INSERT_USUARIO_SQL = "INSERT INTO USUARIO (nome, email, senha) VALUES (?, ?, ?)";
    private static final String INSERT_CLIENTE_SQL = "INSERT INTO CLIENTE (id_usuario, cpf, celular, data_nascimento, endereco) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_CLIENTES_SQL = "SELECT u.id, u.nome, u.email, u.senha, c.cpf, c.celular, c.data_nascimento, c.endereco FROM USUARIO u JOIN CLIENTE c ON u.id = c.id_usuario";
    private static final String SELECT_CLIENTE_BY_ID_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE u.id = ?";
    private static final String SELECT_CLIENTE_BY_CPF_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE c.cpf = ?";
    private static final String SELECT_CLIENTE_BY_EMAIL_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE LOWER(u.email) = LOWER(?)";
    private static final String SELECT_CLIENTES_BY_NOME_SQL = SELECT_ALL_CLIENTES_SQL + " WHERE LOWER(u.nome) LIKE ?";
    private static final String DELETE_CLIENTE_SQL = "DELETE FROM CLIENTE WHERE id_usuario = ?";
    private static final String DELETE_USUARIO_SQL = "DELETE FROM USUARIO WHERE id = ?";
    private static final String COUNT_CLIENTES_SQL = "SELECT COUNT(*) FROM CLIENTE";

    @Override
    public Cliente save(Cliente cliente) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            if (cliente.getId() == 0) {
                // INSERT (novo cliente)
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
                    psCliente.setString(3, cliente.getCelular());
                    if (cliente.getDataNascimento() != null) {
                        psCliente.setDate(4, java.sql.Date.valueOf(cliente.getDataNascimento()));
                    } else {
                        psCliente.setDate(4, null);
                    }
                    psCliente.setString(5, cliente.getEndereco());
                    psCliente.executeUpdate();
                }
            } else {
                // UPDATE (cliente existente)
                // 1. Atualizar tabela USUARIO
                try (PreparedStatement psUsuario = conn.prepareStatement(
                        "UPDATE USUARIO SET nome = ?, email = ?, senha = ? WHERE id = ?")) {
                    psUsuario.setString(1, cliente.getUsuario().getNome());
                    psUsuario.setString(2, cliente.getUsuario().getEmail());
                    psUsuario.setString(3, cliente.getUsuario().getSenha());
                    psUsuario.setInt(4, cliente.getId());
                    psUsuario.executeUpdate();
                }

                // 2. Atualizar tabela CLIENTE
                try (PreparedStatement psCliente = conn.prepareStatement(
                        "UPDATE CLIENTE SET cpf = ?, celular = ?, data_nascimento = ?, endereco = ? WHERE id_usuario = ?")) {
                    psCliente.setString(1, cliente.getCpf());
                    psCliente.setString(2, cliente.getCelular());
                    if (cliente.getDataNascimento() != null) {
                        psCliente.setDate(3, java.sql.Date.valueOf(cliente.getDataNascimento()));
                    } else {
                        psCliente.setDate(3, null);
                    }
                    psCliente.setString(4, cliente.getEndereco());
                    psCliente.setInt(5, cliente.getId());
                    psCliente.executeUpdate();
                }
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
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CLIENTES_BY_NOME_SQL)) {
            pstmt.setString(1, "%" + nome.toLowerCase() + "%");
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

    // Métodos restantes da interface Repository (deleteById, existsById, count) não implementados para brevidade.
    @Override
    public boolean deleteById(Integer id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Remove da tabela CLIENTE
            try (PreparedStatement psCliente = conn.prepareStatement(DELETE_CLIENTE_SQL)) {
                psCliente.setInt(1, id);
                psCliente.executeUpdate();
            }

            // Remove da tabela USUARIO
            try (PreparedStatement psUsuario = conn.prepareStatement(DELETE_USUARIO_SQL)) {
                psUsuario.setInt(1, id);
                int affected = psUsuario.executeUpdate();
                conn.commit();
                return affected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter transação de exclusão: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignore) {}
                DatabaseConnection.closeConnection(conn);
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_CLIENTES_SQL);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar clientes: " + e.getMessage());
        }
        return 0;
    }

    private Cliente mapRowToCliente(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setCpf(rs.getString("cpf"));
        cliente.setCelular(rs.getString("celular"));
        Date dataNascimento = rs.getDate("data_nascimento");
        if (dataNascimento != null) {
            cliente.setDataNascimento(dataNascimento.toLocalDate());
        }
        cliente.setEndereco(rs.getString("endereco"));
        return cliente;
    }
}