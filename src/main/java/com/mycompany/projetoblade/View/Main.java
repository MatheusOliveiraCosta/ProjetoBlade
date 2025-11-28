package com.mycompany.projetoblade.View;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mycompany.projetoblade.repository.VeiculoRepository;
import com.mycompany.projetoblade.repository.VeiculoRepositoryImpl;
import com.mycompany.projetoblade.service.VeiculoService;
import com.mycompany.projetoblade.repository.ClienteDAO;
import com.mycompany.projetoblade.service.ClienteService;
import com.mycompany.projetoblade.repository.VendaRepositoryImpl;
import com.mycompany.projetoblade.service.VendaService;

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

        // Criar repositório/serviço de clientes (compartilhado) - usando ClienteDAO para salvar no banco
        com.mycompany.projetoblade.repository.ClienteRepository clienteRepository = new ClienteDAO();
        ClienteService clienteService = new ClienteService(clienteRepository);

        // Criar repositório/serviço de vendas (compartilhado) e injetar o mesmo veiculoRepository e clienteRepository
        com.mycompany.projetoblade.repository.VendaRepository vendaRepository = new VendaRepositoryImpl();
        VendaService vendaService = new VendaService(vendaRepository, veiculoRepository, clienteRepository);

        // Criar e exibir a tela de catálogo
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blade Motors - Catálogo de Veículos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            TelaCatalogo catalogo = new TelaCatalogo(frame, veiculoService, clienteService, vendaService);
            // ensure the catalog panel is actually used as the frame's content
            frame.setContentPane(catalogo);
            frame.setSize(1280, 800);
            // revalidate/repaint after changing content to avoid a blank/white frame
            frame.revalidate();
            frame.repaint();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }
    
}
