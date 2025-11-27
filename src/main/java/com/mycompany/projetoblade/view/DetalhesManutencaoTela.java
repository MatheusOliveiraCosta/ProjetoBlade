package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.model.Manutencao;
import com.mycompany.projetoblade.model.LaudoTecnico;
import com.mycompany.projetoblade.model.Orcamento;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class DetalhesManutencaoTela extends JDialog {

    private Manutencao manutencao;
    
    // Componentes para edição
    private JTextArea txtDiagnostico;
    private JTextArea txtOrcamento;
    private JTextArea txtLaudo;
    private JComboBox<String> comboStatus;

    public DetalhesManutencaoTela(JFrame parent, Manutencao manutencao) {
        super(parent, true); // Modal
        this.manutencao = manutencao;
        
        setUndecorated(true);
        setSize(600, 750);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0)); // Fundo transparente para o arredondamento

        // Painel Principal (Fundo Cinza Arredondado)
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
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 30, 40));
        mainPanel.setOpaque(false);

        // --- 1. TOPO (Ícone + Info do Cliente + Fechar) ---
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Ícone
        JLabel iconLabel = new JLabel("🔧");
        try {
            java.net.URL imgUrl = getClass().getResource("/images/wrench.png");
            if (imgUrl == null) imgUrl = getClass().getClassLoader().getResource("images/wrench.png");
            if (imgUrl != null) {
                BufferedImage img = ImageIO.read(imgUrl);
                iconLabel.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
                iconLabel.setText("");
            } else {
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            }
        } catch (Exception e) {}
        
        // Título Dinâmico
        String tituloTexto = String.format("<html><b>Cliente:</b> %s<br/><b>Veículo:</b> %s (%s)</html>", 
            manutencao.getFuncionario().getUsuario().getNome(), // Ajuste conforme seu modelo real de Cliente
            manutencao.getVeiculo().getModelo(),
            manutencao.getVeiculo().getPlaca());
            
        // *Nota: No seu modelo atual, Manutencao tem 'Funcionario', mas deveria ter 'Cliente'. 
        // Se tiver 'Cliente', use manutencao.getCliente().getUsuario().getNome()
        // Vou assumir um getCliente() ou usar o Funcionario como placeholder.
        
        JLabel titleLabel = new JLabel(tituloTexto);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(Color.BLACK);

        // Botão Fechar
        JLabel btnFechar = new JLabel("✕");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { dispose(); }
        });

        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(btnFechar, BorderLayout.EAST);

        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // --- 2. TÍTULO DA SEÇÃO ---
        JLabel lblConserto = new JLabel("Conserto:");
        lblConserto.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblConserto.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblConserto);
        mainPanel.add(Box.createVerticalStrut(20));

        // --- 3. CAMPOS DE EDIÇÃO ---
        
        // Problema/Diagnóstico
        mainPanel.add(criarLabel("Problema/Diagnóstico:"));
        txtDiagnostico = criarTextArea(manutencao.getDescricao());
        mainPanel.add(wrapInScroll(txtDiagnostico));
        mainPanel.add(Box.createVerticalStrut(15));

        // Orçamento (Valor e Peças)
        mainPanel.add(criarLabel("Orçamento:"));
        String textoOrcamento = (manutencao.getOrcamento() != null) ? 
            manutencao.getOrcamento().getDetalhesServico() + " - R$ " + manutencao.getOrcamento().getValor() : "";
        txtOrcamento = criarTextArea(textoOrcamento);
        mainPanel.add(wrapInScroll(txtOrcamento));
        mainPanel.add(Box.createVerticalStrut(15));

        // Laudo Técnico
        mainPanel.add(criarLabel("Laudo Técnico:"));
        String textoLaudo = (manutencao.getLaudoTecnico() != null) ? 
            manutencao.getLaudoTecnico().getDescricaoServico() : "";
        txtLaudo = criarTextArea(textoLaudo);
        mainPanel.add(wrapInScroll(txtLaudo));
        mainPanel.add(Box.createVerticalStrut(20));

        // --- 4. ATUALIZAÇÃO DE STATUS ---
        mainPanel.add(criarLabel("Atualizar status"));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setOpaque(false);
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Combo de Status
        String[] statusOpcoes = {"Aguardando", "Em Diagnóstico", "Aguardando Aprovação", "Em Andamento", "Concluído", "Cancelado"};
        comboStatus = new JComboBox<>(statusOpcoes);
        comboStatus.setSelectedItem(manutencao.getStatus()); // Seleciona o atual
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboStatus.setBackground(Color.WHITE);
        comboStatus.setPreferredSize(new Dimension(200, 40));
        
        // Botão Salvar
        JButton btnAtualizar = new JButton("Atualizar status");
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setBackground(new Color(0, 168, 89)); // Verde
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.setPreferredSize(new Dimension(150, 40));
        btnAtualizar.putClientProperty(FlatClientProperties.STYLE, "arc: 10;");
        
        btnAtualizar.addActionListener(e -> salvarAlteracoes());

        statusPanel.add(comboStatus);
        statusPanel.add(Box.createHorizontalStrut(15));
        statusPanel.add(btnAtualizar);
        
        mainPanel.add(statusPanel);

        setContentPane(mainPanel);
    }

    // --- LÓGICA DE SALVAR ---
    private void salvarAlteracoes() {
        // 1. Atualiza o objeto Manutencao na memória
        manutencao.setDescricao(txtDiagnostico.getText());
        manutencao.setStatus((String) comboStatus.getSelectedItem());
        
        // 2. Atualiza ou cria o Orçamento
        if (!txtOrcamento.getText().isEmpty()) {
            if (manutencao.getOrcamento() == null) manutencao.setOrcamento(new Orcamento());
            manutencao.getOrcamento().setDetalhesServico(txtOrcamento.getText());
            // Lógica de parsing do valor R$ seria necessária aqui em um app real
        }
        
        // 3. Atualiza ou cria o Laudo
        if (!txtLaudo.getText().isEmpty()) {
            if (manutencao.getLaudoTecnico() == null) manutencao.setLaudoTecnico(new LaudoTecnico());
            manutencao.getLaudoTecnico().setDescricaoServico(txtLaudo.getText());
        }
        
        // 4. Feedback e Fechar
        JOptionPane.showMessageDialog(this, "Manutenção atualizada com sucesso!");
        dispose();
    }

    // --- HELPERS VISUAIS ---
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextArea criarTextArea(String textoInicial) {
        JTextArea area = new JTextArea(textoInicial);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private JScrollPane wrapInScroll(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Altura fixa para os campos
        scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Estilo arredondado do FlatLaf no ScrollPane (que age como a borda do input)
        scroll.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" +
            "background: #FFFFFF;" +
            "borderWidth: 0;");
            
        return scroll;
    }
}