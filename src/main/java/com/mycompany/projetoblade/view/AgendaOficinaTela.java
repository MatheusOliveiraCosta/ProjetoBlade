package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Manutencao;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.model.Mecanico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class AgendaOficinaTela extends JDialog {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Manutencao> listaManutencoes;

    public AgendaOficinaTela(JFrame parent) {
        super(parent, true); // Modal
        setUndecorated(true);
        setSize(1000, 700); // Tela larga para caber a tabela
        setLocationRelativeTo(parent);
        
        // Configuração de fundo transparente para bordas arredondadas
        setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD9D9D9)); // Cinza claro
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        // --- 1. TOPO (Título e Fechar) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(0, 80));
        
        // Botão X
        JLabel btnFechar = new JLabel("✕");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { dispose(); }
        });
        
        // Título e Ícone Centralizados
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);
        
        JLabel iconLabel = new JLabel("🛠️"); // Fallback emoji
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tenta carregar icone wrench.png se tiver
        try {
            java.net.URL imgUrl = getClass().getResource("/images/wrench.png");
            if (imgUrl == null) imgUrl = getClass().getClassLoader().getResource("images/wrench.png");
            if (imgUrl != null) {
                BufferedImage img = ImageIO.read(imgUrl);
                iconLabel.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
                iconLabel.setText("");
            }
        } catch (Exception e) {}

        JLabel titleLabel = new JLabel("Manutenções");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titleContainer.add(iconLabel);
        titleContainer.add(Box.createVerticalStrut(5));
        titleContainer.add(titleLabel);
        
        topPanel.add(btnFechar, BorderLayout.EAST); // X na direita
        topPanel.add(titleContainer, BorderLayout.CENTER); // Título no centro (o BorderLayout vai esticar, mas visualmente ok)
        // Gambiarra para centralizar visualmente o título (compensar o botão X)
        JLabel dummy = new JLabel("   "); 
        dummy.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(dummy, BorderLayout.WEST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- 2. FILTRO E TABELA ---
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Painel de Filtro
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        
        JLabel lblFiltro = new JLabel("Filtrar por status:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JComboBox<String> comboStatus = new JComboBox<>(new String[]{"Todos", "Aguardando", "Em Andamento", "Concluído", "Cancelado"});
        comboStatus.setBackground(Color.WHITE);
        comboStatus.setFocusable(false);
        
        filterPanel.add(lblFiltro);
        filterPanel.add(comboStatus);
        contentPanel.add(filterPanel, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"O.S. (ID)", "DATA", "CLIENTES (Clique para detalhes)", "VEICULO (PLACA)", "MECÂNICO", "STATUS"};
        
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Nenhuma célula editável
            }
        };

        tabela = new JTable(modeloTabela);
        estilizarTabela();

        // Lógica de Clique na Tabela
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabela.rowAtPoint(e.getPoint());
                int col = tabela.columnAtPoint(e.getPoint());
                
                // Se clicou na coluna de CLIENTES (Índice 2) e a linha é válida
                if (row >= 0 && col == 2) {
                    abrirDetalhesManutencao(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(new LineBorder(Color.BLACK, 1)); // Borda preta fina na tabela
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Carregar dados mockados
        carregarDadosExemplo();
    }

    private void estilizarTabela() {
        tabela.setRowHeight(35);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setShowVerticalLines(true);
        tabela.setShowHorizontalLines(true);
        tabela.setGridColor(Color.LIGHT_GRAY);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(Color.WHITE);
        tabela.getTableHeader().setReorderingAllowed(false);
        
        // Centralizar todas as colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Destaque visual na coluna de Cliente (para indicar que é clicável)
        tabela.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(new Color(0, 102, 204)); // Azul de link
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setText("<html><u>" + value + "</u></html>"); // Sublinhado
                return this;
            }
        });
    }

    private void carregarDadosExemplo() {
        listaManutencoes = new ArrayList<>();
        
        // Criando dados mockados
        Usuario u1 = new Usuario("João da Silva", "joao@email.com", "123");
        Cliente c1 = new Cliente("Rua A", "111.111.111-11", u1);
        c1.setId(1); // Importante para vínculo
        
        Veiculo v1 = new Veiculo("Fiat Uno", "Fiat", 2020, "ABC-1234", "CH1");
        v1.setDono(c1);
        
        Usuario uMec = new Usuario("Carlos Mecânico", "carlos@mec.com", "123");
        Mecanico m1 = new Mecanico("M001", LocalDate.now(), uMec, "Geral");
        
        // Adiciona 15 itens para testar o scroll
        for (int i = 1; i <= 15; i++) {
            String status = (i % 3 == 0) ? "Concluído" : (i % 2 == 0 ? "Em Andamento" : "Aguardando");
            Manutencao m = new Manutencao(LocalDate.now().minusDays(i), "Revisão " + i, status, v1, m1);
            m.setIdManutencao(100 + i);
            listaManutencoes.add(m);
            
            modeloTabela.addRow(new Object[]{
                m.getIdManutencao(),
                m.getDataAgendamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                c1.getUsuario().getNome(), // Nome do cliente
                v1.getModelo() + " (" + v1.getPlaca() + ")",
                m1.getUsuario().getNome(),
                status
            });
        }
    }

    private void abrirDetalhesManutencao(int row) {
        Manutencao manutencaoSelecionada = listaManutencoes.get(row);
        
        // Abre a tela de detalhes que desenhamos antes
        // (Certifique-se de ter criado a classe DetalhesManutencaoTela)
        try {
            DetalhesManutencaoTela detalhes = new DetalhesManutencaoTela((JFrame) SwingUtilities.getWindowAncestor(this), manutencaoSelecionada);
            detalhes.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Tela de Detalhes ainda não implementada!\nID: " + manutencaoSelecionada.getIdManutencao());
        }
    }
    
    // Método estático para chamar a tela
    public static void mostrar(JFrame parent) {
        new AgendaOficinaTela(parent).setVisible(true);
    }

    
}