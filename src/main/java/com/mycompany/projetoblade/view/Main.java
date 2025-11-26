package com.mycompany.projetoblade.view;

import com.mycompany.projetoblade.repository.VeiculoRepository;
import com.mycompany.projetoblade.repository.VeiculoRepositoryImpl;
import com.mycompany.projetoblade.service.VeiculoService;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe principal da aplicação.
 */
public class Main {
    
    public static void main(String[] args) {
        // Configurar Look and Feel do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Erro ao configurar Look and Feel: " + ex.getMessage());
        }
        
        // Primeiro cria o Repositório
        VeiculoRepository veiculoRepository = new VeiculoRepositoryImpl();
        
        // Depois cria o Serviço passando o Repositório
        VeiculoService veiculoService = new VeiculoService(veiculoRepository);

        // Criar e exibir a tela de catálogo
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blade Motors - Catálogo de Veículos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            TelaCatalogo catalogo = new TelaCatalogo(frame, veiculoService);
            frame.setSize(1280, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }
}
