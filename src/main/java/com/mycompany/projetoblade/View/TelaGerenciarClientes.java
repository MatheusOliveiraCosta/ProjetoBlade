package com.mycompany.projetoblade.view;

import java.awt.BorderLayout;
import java.awt.Color; // Importe seu modelo real aqui
import java.awt.Cursor; // Importe seu modelo real aqui
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.service.ClienteService;

public class TelaGerenciarClientes extends JDialog {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Cliente> listaClientes; // Lista em memória simulando o banco
    private JFrame parentFrame;
    private ClienteService clienteService;

    public TelaGerenciarClientes(JFrame parent, ClienteService clienteService) {
        super(parent, true); // Modal
        this.parentFrame = parent;
        this.clienteService = clienteService;
        setUndecorated(true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        // Painel Principal
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD9D9D9));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        mainPanel.setOpaque(false);

        // --- 1. TOPO (Título, Busca e Fechar) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Título e Ícone
        JLabel titleLabel = new JLabel("Clientes Cadastrados", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setIcon(new ImageIcon(getClass().getResource("/images/user.png"))); // Use seu ícone se tiver
        // Se não tiver ícone, comente a linha acima ou use emoji: titleLabel.setText("👥 Clientes Cadastrados");

        // Botão Fechar
        JLabel btnFechar = new JLabel("✕");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { dispose(); }
        });

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(btnFechar, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- 2. TABELA (Centro) ---
        // Colunas na ordem solicitada
        String[] colunas = {"NOME", "CPF", "CELULAR", "EMAIL", "ENDEREÇO"};
        
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloqueia edição direta
            }
        };

        tabela = new JTable(modeloTabela);
        estilizarTabela();

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- 3. BOTÕES DE AÇÃO (Rodapé) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnNovo = criarBotao("Novo Cliente", new Color(60, 60, 60));
        JButton btnEditar = criarBotao("Editar", new Color(60, 60, 60)); // Cinza escuro do seu design
        JButton btnExcluir = criarBotao("Excluir", new Color(60, 60, 60));

        // AÇÕES DOS BOTÕES

        // Novo Cliente
        btnNovo.addActionListener(e -> {
            // Abre a tela de cadastro (que você já tem)
            CadastroClienteTela.mostrar(parentFrame, clienteService); 
            // Após fechar, recarrega a tabela
            carregarDados();
        });

        // Editar
        btnEditar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Pega o objeto cliente selecionado
            Cliente clienteSelecionado = listaClientes.get(row);
            
            // Abre a tela de cadastro em modo edição
            CadastroClienteTela.editar(parentFrame, clienteService, clienteSelecionado);
            // Após fechar, recarrega a tabela
            carregarDados();
        });

        // Excluir
        btnExcluir.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Cliente clienteSelecionado = listaClientes.get(row);
            String nome = clienteSelecionado.getUsuario() != null ? clienteSelecionado.getUsuario().getNome() : "Cliente";
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir " + nome + "?", "Excluir", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Remove do repositório usando o serviço
                    boolean removido = clienteService.removerCliente(clienteSelecionado.getId());
                    if (removido) {
                        JOptionPane.showMessageDialog(this, "Cliente removido com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        // Recarrega a tabela
                        carregarDados();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao remover cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao remover cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnNovo);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Carrega dados iniciais
        carregarDados();
    }

    private JButton criarBotao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(corFundo);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10;");
        return btn;
    }

    private void estilizarTabela() {
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(new Color(230, 230, 230));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Centralizar textos das colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT); // Alinhado à esquerda fica melhor para leitura
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void carregarDados() {
        // Limpa tabela
        modeloTabela.setRowCount(0);
        listaClientes = new ArrayList<>();

        try {
            // Carrega dados reais do serviço
            listaClientes = clienteService.listarTodos();
            
            // Preenche a tabela com os dados reais
            for (Cliente c : listaClientes) {
                String celular = c.getCelular() != null ? c.getCelular() : "N/A";
                String nome = c.getUsuario() != null ? c.getUsuario().getNome() : "N/A";
                String email = c.getUsuario() != null ? c.getUsuario().getEmail() : "N/A";
                String endereco = c.getEndereco() != null ? c.getEndereco() : "N/A";
                String cpf = c.getCpf() != null ? c.getCpf() : "N/A";
                
                modeloTabela.addRow(new Object[]{
                    nome,
                    cpf,
                    celular,
                    email,
                    endereco
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}