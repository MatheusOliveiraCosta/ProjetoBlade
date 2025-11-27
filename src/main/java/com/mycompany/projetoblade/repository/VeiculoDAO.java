package com.mycompany.projetoblade.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.utils.DatabaseConnection;

public class VeiculoDAO implements VeiculoRepository {

    private static final String INSERT_SQL = "INSERT INTO VEICULO (modelo, marca, ano, placa, chassi, status, preco) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE VEICULO SET modelo = ?, marca = ?, ano = ?, placa = ?, chassi = ?, status = ?, preco = ? WHERE idVeiculo = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM VEICULO";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM VEICULO WHERE idVeiculo = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM VEICULO WHERE idVeiculo = ?";
    private static final String SELECT_BY_MARCA_SQL = "SELECT * FROM VEICULO WHERE marca = ?";
    private static final String SELECT_BY_MODELO_SQL = "SELECT * FROM VEICULO WHERE modelo = ?";
    private static final String SELECT_BY_ANO_SQL = "SELECT * FROM VEICULO WHERE ano = ?";
    private static final String SELECT_BY_STATUS_SQL = "SELECT * FROM VEICULO WHERE status = ?";
    private static final String SELECT_BY_PLACA_SQL = "SELECT * FROM VEICULO WHERE placa = ?";
    private static final String SELECT_BY_PRECO_SQL = "SELECT * FROM VEICULO WHERE preco BETWEEN ? AND ?";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM VEICULO";

    @Override
    public Veiculo save(Veiculo veiculo) {
        String sql = (veiculo.getIdVeiculo() == 0) ? INSERT_SQL : UPDATE_SQL;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, veiculo.getModelo());
            pstmt.setString(2, veiculo.getMarca());
            pstmt.setInt(3, veiculo.getAno());
            pstmt.setString(4, veiculo.getPlaca());
            pstmt.setString(5, veiculo.getChassi());
            pstmt.setString(6, veiculo.getStatus());
            pstmt.setDouble(7, veiculo.getPreco());

            if (veiculo.getIdVeiculo() != 0) {
                pstmt.setInt(8, veiculo.getIdVeiculo());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0 && veiculo.getIdVeiculo() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        veiculo.setIdVeiculo(rs.getInt(1));
                    }
                }
            }
            return veiculo;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar veículo: " + e.getMessage());
            // Em uma aplicação real, seria melhor lançar uma exceção customizada.
            return null;
        }
    }

    @Override
    public Optional<Veiculo> findById(Integer id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToVeiculo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar veículo por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Veiculo> findAll() {
        List<Veiculo> veiculos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                veiculos.add(mapRowToVeiculo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar veículos: " + e.getMessage());
        }
        return veiculos;
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_ID_SQL)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar veículo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_SQL);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar veículos: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public List<Veiculo> findByMarca(String marca) {
        return findByAttribute(SELECT_BY_MARCA_SQL, marca);
    }

    @Override
    public List<Veiculo> findByModelo(String modelo) {
        return findByAttribute(SELECT_BY_MODELO_SQL, modelo);
    }

    @Override
    public List<Veiculo> findByAno(int ano) {
        return findByAttribute(SELECT_BY_ANO_SQL, ano);
    }

    @Override
    public List<Veiculo> findByStatus(String status) {
        return findByAttribute(SELECT_BY_STATUS_SQL, status);
    }

    @Override
    public Optional<Veiculo> findByPlaca(String placa) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_PLACA_SQL)) {
            pstmt.setString(1, placa);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToVeiculo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar veículo por placa: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Veiculo> findByPrecoBetween(double precoMin, double precoMax) {
        List<Veiculo> veiculos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_PRECO_SQL)) {
            pstmt.setDouble(1, precoMin);
            pstmt.setDouble(2, precoMax);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                veiculos.add(mapRowToVeiculo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar veículos por preço: " + e.getMessage());
        }
        return veiculos;
    }

    @Override
    public List<Veiculo> findByDono(int clienteId) {
        // Esta funcionalidade requer um JOIN com a tabela de Venda ou uma coluna de dono no Veiculo.
        // Assumindo que não existe, retornará uma lista vazia.
        System.err.println("Método findByDono não implementado para JDBC neste exemplo.");
        return new ArrayList<>();
    }

    private List<Veiculo> findByAttribute(String sql, Object attribute) {
        List<Veiculo> veiculos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, attribute);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                veiculos.add(mapRowToVeiculo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro na busca: " + e.getMessage());
        }
        return veiculos;
    }

    private Veiculo mapRowToVeiculo(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setIdVeiculo(rs.getInt("idVeiculo"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setChassi(rs.getString("chassi"));
        veiculo.setStatus(rs.getString("status"));
        veiculo.setPreco(rs.getDouble("preco"));
        return veiculo;
    }
}