package com.mycompany.projetoblade.repository;

import com.mycompany.projetoblade.model.Venda;
import com.mycompany.projetoblade.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendaDAO implements VendaRepository {

    private static final String INSERT_SQL = "INSERT INTO VENDA (id_cliente, id_veiculo, data_venda, valor_final) VALUES (?, ?, ?, ?)";

    @Override
    public Venda save(Venda venda) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, venda.getCliente().getId());
            pstmt.setInt(2, venda.getVeiculo().getIdVeiculo());
            pstmt.setDate(3, java.sql.Date.valueOf(venda.getDataVenda()));
            pstmt.setDouble(4, venda.getValorFinal());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        venda.setIdVenda(rs.getInt(1));
                    }
                }
            }
            return venda;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar venda: " + e.getMessage());
            return null;
        }
    }

    // Métodos de busca e outros não implementados para brevidade.
    // A implementação seguiria o padrão visto em VeiculoDAO (SELECT com JOINs para buscar cliente e veículo).
    @Override
    public Optional<Venda> findById(Integer id) { return Optional.empty(); }
    @Override
    public List<Venda> findAll() { return new ArrayList<>(); }
    @Override
    public boolean deleteById(Integer id) { return false; }
    @Override
    public boolean existsById(Integer id) { return false; }
    @Override
    public long count() { return 0; }
    @Override
    public List<Venda> findByClienteId(Integer clienteId) { return new ArrayList<>(); }
    @Override
    public List<Venda> findByVeiculoId(Integer veiculoId) { return new ArrayList<>(); }
    @Override
    public List<Venda> findByDataVendaBetween(LocalDate dataInicio, LocalDate dataFim) { return new ArrayList<>(); }
    @Override
    public List<Venda> findByValorFinalGreaterThanEqual(double valorMin) { return new ArrayList<>(); }
}