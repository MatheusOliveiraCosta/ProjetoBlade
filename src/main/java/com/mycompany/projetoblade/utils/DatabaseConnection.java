package com.mycompany.projetoblade.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Configurações do Banco de Dados
    private static final String URL = "jdbc:mysql://localhost:3306/blade_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "nelson"; // Mude para seu usuário do DB
    private static final String PASSWORD = "abc123"; // Mude para sua senha do DB

    /**
     * Estabelece e retorna uma nova conexão com o banco de dados.
     * @return Objeto Connection
     * @throws SQLException Se ocorrer um erro de conexão.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Opcional: Carregar o driver (em versões modernas do JDBC não é estritamente necessário)
            // Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fecha a conexão com o banco de dados.
     * @param connection A conexão a ser fechada.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}