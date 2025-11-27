package com.mycompany.projetoblade;

import java.util.List;

import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.repository.VeiculoDAO;

/**
 * Classe principal para testes de integração com o banco de dados.
 */
public class Main {
    public static void main(String[] args) {
        VeiculoDAO veiculoDAO = new VeiculoDAO();

        // 1. Criar um novo veículo para teste
        // O construtor de Veiculo pode variar. Ajuste se necessário.
        Veiculo novoVeiculo = new Veiculo("Modelo Z", "Marca W", 2024, "XYZ-5678", "CHASSI987654321");
        novoVeiculo.setStatus("Disponível");
        novoVeiculo.setPreco(200000.00);
        
        veiculoDAO.save(novoVeiculo);

        // 2. Listar todos os veículos do banco de dados
        List<Veiculo> veiculos = veiculoDAO.findAll();
        System.out.println("\n--- Veículos encontrados no banco de dados ---");
        for (Veiculo v : veiculos) {
            System.out.println("ID: " + v.getIdVeiculo() + ", Modelo: " + v.getModelo() + ", Marca: " + v.getMarca() + ", Preço: " + v.getPreco());
        }
    }
}