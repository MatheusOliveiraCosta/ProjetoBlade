package com.mycompany.projetoblade.view;

import com.mycompany.projetoblade.controller.VeiculoController;
import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.repository.VeiculoRepository;
import com.mycompany.projetoblade.repository.VeiculoRepositoryImpl;
import com.mycompany.projetoblade.service.VeiculoService;
import com.mycompany.projetoblade.view.VeiculoFormTela; // Importe a sua tela

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        // SwingUtilities.invokeLater garante que a UI seja criada na thread correta
        SwingUtilities.invokeLater(() -> {
            
            // --- 1. INICIALIZAÇÃO DO BACK-END (igual ao que você já tinha) ---
            VeiculoRepository veiculoRepository = new VeiculoRepositoryImpl();
            VeiculoService veiculoService = new VeiculoService(veiculoRepository);
            VeiculoController veiculoController = new VeiculoController(veiculoService);

            // --- 2. CRIAÇÃO DA VIEW ---
            VeiculoFormTela view = new VeiculoFormTela();

            // --- 3. CONEXÃO ENTRE VIEW E CONTROLLER (A MÁGICA DO MVC) ---
            // Adicionamos um "ouvinte" ao botão "Salvar" da tela.
            // Quando o botão for clicado, o código dentro deste listener será executado.
            view.addSalvarListener(e -> {
                try {
                    // a. Pega os dados da View
                    String marca = view.getMarca();
                    String modelo = view.getModelo();
                    String placa = view.getPlaca();
                    int ano = Integer.parseInt(view.getAno()); // Converte o texto do ano para número
                    
                    // b. Cria um objeto Model com os dados
                    // (Vamos simplificar e não pegar o chassi por enquanto)
                    Veiculo novoVeiculo = new Veiculo(modelo, marca, ano, placa, "CHASSI_PADRAO");
                    novoVeiculo.setPreco(50000.0); // Preço de exemplo

                    // c. Envia o objeto para o Controller
                    veiculoController.adicionarVeiculo(novoVeiculo);
                    
                    // d. Dá um feedback visual para o usuário
                    JOptionPane.showMessageDialog(view, "Veículo salvo com sucesso!");
                    
                    // Opcional: Imprime a lista no console para confirmar
                    System.out.println("--- LISTA ATUAL DE VEÍCULOS ---");
                    veiculoController.listarVeiculos().forEach(System.out::println);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "O ano deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(view, "Erro de Validação: " + ex.getMessage(), "Erro de Dados", JOptionPane.ERROR_MESSAGE);
                }
            });

            // --- 4. INICIA A APLICAÇÃO ---
            // Torna a janela visível para o usuário
            view.setVisible(true);
        });
    }
}
