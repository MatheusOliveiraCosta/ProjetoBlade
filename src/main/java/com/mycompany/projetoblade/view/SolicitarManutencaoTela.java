package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.service.ManutencaoService;
import com.mycompany.projetoblade.service.VeiculoService;
import com.mycompany.projetoblade.model.Manutencao;
import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.utils.Sessao;
import com.mycompany.projetoblade.model.Cliente;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SolicitarManutencaoTela extends JDialog {

    private VeiculoService veiculoService;
    private ManutencaoService manutencaoService;
    private JTextField placaField;
    private JFormattedTextField txtData;

    public SolicitarManutencaoTela(JFrame parent, VeiculoService veiculoService, ManutencaoService manutencaoService) {
        super(parent, true); // Modal
        setUndecorated(true); // Remove a barra de título padrão
        setSize(500, 650);
        setLocationRelativeTo(parent); // Centraliza na tela
        
        // Configura o fundo transparente para que o arredondamento funcione
        setBackground(new Color(0, 0, 0, 0));
        
        // Painel Principal com fundo Cinza e Bordas Arredondadas
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD9D9D9)); // Cor de fundo cinza claro
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 40, 40)); // Margens internas
        mainPanel.setOpaque(false);

        // --- 1. TOPO (Botão Fechar) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel btnFechar = new JLabel("✕");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });
        topPanel.add(btnFechar);
        mainPanel.add(topPanel);

        // --- 2. ÍCONE E TÍTULO ---
        JLabel iconLabel = new JLabel("🔧"); // Fallback emoji
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tenta carregar a imagem da chave se existir
        try {
            java.net.URL imgUrl = getClass().getResource("/images/wrench.png");
            if (imgUrl == null) imgUrl = getClass().getClassLoader().getResource("images/wrench.png");
            if (imgUrl != null) {
                BufferedImage img = ImageIO.read(imgUrl);
                Image scaled = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaled));
                iconLabel.setText(""); // Remove emoji se imagem carregar
            }
        } catch (Exception e) { /* Ignora erro e usa emoji */ }
        
        mainPanel.add(iconLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel titulo = new JLabel("Solicitar Manutenção");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(40));

        // --- 3. FORMULÁRIO ---
        
        this.veiculoService = veiculoService;
        this.manutencaoService = manutencaoService;

        // Campo Placa
        mainPanel.add(criarLabel("Placa:"));
        this.placaField = criarTextField();
        mainPanel.add(this.placaField);
        mainPanel.add(Box.createVerticalStrut(15));

        // Campo Descrição
        mainPanel.add(criarLabel("Descrição do problema:"));
        JTextArea txtDescricao = new JTextArea(4, 20);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        scrollDesc.setBorder(null); // Remove borda padrão
        // Estiliza o scrollpane para parecer um input arredondado
        scrollDesc.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderColor: #ffffff; background: #ffffff");
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        mainPanel.add(scrollDesc);
        mainPanel.add(Box.createVerticalStrut(15));

        // Campo Data
        mainPanel.add(criarLabel("Data desejada:"));
        txtData = null;
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(dateMask);
        } catch (Exception e) {
            txtData = new JFormattedTextField();
        }
        estilizarTextField(txtData); // Aplica o mesmo estilo
        mainPanel.add(txtData);
        mainPanel.add(Box.createVerticalStrut(40));

        // --- 4. BOTÕES ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        // Botão Solicitar (Verde)
        JButton btnSolicitar = new JButton("Solicitar");
        btnSolicitar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSolicitar.setForeground(Color.WHITE);
        btnSolicitar.setBackground(new Color(0, 168, 89)); // #00A859
        btnSolicitar.setBorderPainted(false);
        btnSolicitar.setFocusPainted(false);
        btnSolicitar.setPreferredSize(new Dimension(140, 45));
        btnSolicitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Arredondamento
        btnSolicitar.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        
        btnSolicitar.addActionListener(e -> {
            try {
                String placa = placaField.getText().trim();
                String descricao = txtDescricao.getText().trim();
                String dataTxt = txtData.getText().trim();
                LocalDate data = null;
                try {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    data = LocalDate.parse(dataTxt, fmt);
                } catch (Exception ex) {
                    data = LocalDate.now();
                }

                Veiculo v = null;
                if (veiculoService != null && placa != null && !placa.isEmpty()) {
                    v = veiculoService.buscarPorPlaca(placa).orElse(null);
                }

                Manutencao m = new Manutencao();
                m.setDataAgendamento(data);
                m.setDescricao(descricao);
                m.setStatus("AGUARDANDO"); // Regra: inicial AGUARDANDO
                m.setVeiculo(v);

                // se existir serviço, salva
                if (manutencaoService != null) {
                    manutencaoService.salvarManutencao(m);
                }

                JOptionPane.showMessageDialog(this,
                        "Solicitação enviada com sucesso!\nAguarde o contato da oficina.",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao solicitar manutenção: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botão Cancelar (Vermelho)
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(255, 68, 68)); // #FF4444
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(140, 45));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnSolicitar);
        buttonPanel.add(btnCancelar);
        
        mainPanel.add(buttonPanel);

        setContentPane(mainPanel);
    }

    // --- MÉTODOS AUXILIARES DE ESTILO ---

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField criarTextField() {
        JTextField field = new JTextField();
        estilizarTextField(field);
        return field;
    }

    private void estilizarTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // Borda arredondada e padding interno
        field.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" +
            "borderWidth: 0;" +
            "margin: 0, 10, 0, 10"); // top, left, bottom, right padding
    }
    
    // Conveniência: construtor com apenas parent (mantém compatibilidade)
    public SolicitarManutencaoTela(JFrame parent) {
        this(parent, null, null);
    }

    // Conveniência: mostra a tela já com a placa preenchida
    public static void mostrar(JFrame parent, String placa) {
        SwingUtilities.invokeLater(() -> {
            SolicitarManutencaoTela tela = new SolicitarManutencaoTela(parent);
            if (tela.placaField != null && placa != null) tela.placaField.setText(placa);
            tela.setVisible(true);
        });
    }

    /**
     * Mostra a tela com serviços injetados (opcionalmente passando placa).
     */
    public static void mostrar(JFrame parent, VeiculoService veiculoService, ManutencaoService manutencaoService) {
        SwingUtilities.invokeLater(() -> {
            SolicitarManutencaoTela tela = new SolicitarManutencaoTela(parent, veiculoService, manutencaoService);
            tela.setVisible(true);
        });
    }

    public static void mostrar(JFrame parent, VeiculoService veiculoService, ManutencaoService manutencaoService, String placa) {
        SwingUtilities.invokeLater(() -> {
            SolicitarManutencaoTela tela = new SolicitarManutencaoTela(parent, veiculoService, manutencaoService);
            if (tela.placaField != null && placa != null) tela.placaField.setText(placa);
            tela.setVisible(true);
        });
    }

    /** Set placa programmatically (safe accessor) */
    public void setPlaca(String placa) {
        if (this.placaField != null && placa != null) this.placaField.setText(placa);
    }

    // Método estático para facilitar a chamada
    public static void mostrar(JFrame parent) {
        SwingUtilities.invokeLater(() -> {
            new SolicitarManutencaoTela(parent).setVisible(true);
        });
    }
}



