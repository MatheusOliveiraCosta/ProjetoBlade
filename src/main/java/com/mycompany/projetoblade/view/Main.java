package com.mycompany.projetoblade.view;

import javax.swing.*;

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
        
        // Criar e exibir a tela de catálogo
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blade Motors - Catálogo de Veículos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new TelaCatalogo(frame));
            frame.setSize(1280, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
