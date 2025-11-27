package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.model.Cliente; // Importe seu modelo real aqui
import com.mycompany.projetoblade.model.Usuario; // Importe seu modelo real aqui
// Se não tiver os modelos ainda, use as classes internas abaixo ou ajuste os imports.

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class TelaGerenciarClientes extends JDialog {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Cliente> listaClientes; // Lista em memória simulando o banco
    private JFrame parentFrame;

    public TelaGerenciarClientes(JFrame parent) {
        super(parent, true); // Modal
        this.parentFrame = parent;
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
            CadastroClienteTela.mostrar(parentFrame); 
            // Após fechar, recarrega a tabela (simulado)
            carregarDadosMock();
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
            
            // AQUI: Você abriria a tela de cadastro preenchida com os dados desse cliente
            JOptionPane.showMessageDialog(this, "Editando: " + clienteSelecionado.getUsuario().getNome() + "\n(Funcionalidade de preencher form pendente)");
            // CadastroClienteTela.editar(parentFrame, clienteSelecionado);
        });

        // Excluir
        btnExcluir.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nome = (String) tabela.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir " + nome + "?", "Excluir", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Remove da lista e da tabela
                listaClientes.remove(row);
                modeloTabela.removeRow(row);
                JOptionPane.showMessageDialog(this, "Cliente removido.");
            }
        });

        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnNovo);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Carrega dados iniciais
        carregarDadosMock();
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

    private void carregarDadosMock() {
        // Limpa tabela
        modeloTabela.setRowCount(0);
        listaClientes = new ArrayList<>();

        // Cria dados fictícios completos para teste
        // (Adapte isso para usar seu ClienteService no futuro)
        for (int i = 1; i <= 5; i++) {
            Usuario u = new Usuario("Cliente Teste " + i, "email" + i + "@teste.com", "123");
            Cliente c = new Cliente("Rua Exemplo, " + i * 10, "111.222.333-0" + i, u);
            // Supondo que Cliente tenha o campo celular, se não tiver, adicione no seu Model
            // c.setCelular("(41) 99999-000" + i); 
            
            listaClientes.add(c);

            modeloTabela.addRow(new Object[]{
                c.getUsuario().getNome(),
                c.getCpf(),
                "(41) 99999-000" + i, // Celular mockado se não tiver no model
                c.getUsuario().getEmail(),
                c.getEndereco()
            });
        }
        // Adiciona o caso da imagem
        modeloTabela.addRow(new Object[]{"Matheus Oliveira", "111.803.699-98", "(41) 99876-5432", "matheus@gmail.com", "Parana"});
    }
}