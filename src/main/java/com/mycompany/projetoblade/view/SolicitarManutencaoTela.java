package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

/**
 * Tela de solicitação de manutenção - Modal com design moderno
 */
public class SolicitarManutencaoTela extends JDialog {
    
    private JTextField campoPlaca;
    private JTextArea campoDescricao;
    private JFormattedTextField campoData;
    
    public SolicitarManutencaoTela(JFrame parent) {
        super(parent, true); // Modal
        setUndecorated(true); // Remove barra de título padrão
        setSize(500, 650);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Fundo cinza claro
        getContentPane().setBackground(new Color(0xE0E0E0));
        
        // Aplicar bordas arredondadas na janela
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 500, 650, 15, 15));
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0xE0E0E0));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // === TOPO ===
        // Painel do topo com botão X e ícone
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Botão X (fechar) no canto superior direito
        JButton btnFechar = new JButton("✕");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnFechar.setForeground(Color.BLACK);
        btnFechar.setBorderPainted(false);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setFocusPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());
        topPanel.add(btnFechar, BorderLayout.EAST);
        
        // Ícone de ferramenta centralizado
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        try {
            java.net.URL iconUrl = getClass().getResource("/images/wrench.png");
            if (iconUrl == null) {
                iconUrl = getClass().getClassLoader().getResource("images/wrench.png");
            }
            
            if (iconUrl != null) {
                java.awt.image.BufferedImage imgIcon = javax.imageio.ImageIO.read(iconUrl);
                int targetSize = 40;
                Image scaledIcon = imgIcon.getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaledIcon));
            } else {
                // Fallback: emoji de ferramenta
                iconLabel.setText("🔧");
                iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            }
        } catch (Exception e) {
            iconLabel.setText("🔧");
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        }
        
        topPanel.add(iconLabel, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(topPanel, gbc);
        
        // Título "Solicitar Manutenção"
        JLabel titulo = new JLabel("Solicitar Manutenção");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.BLACK);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 30, 0);
        mainPanel.add(titulo, gbc);
        
        // === CAMPOS DO FORMULÁRIO ===
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridwidth = 1;
        
        // Placa
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(criarLabel("Placa:"), gbc);
        
        gbc.gridy = 3;
        campoPlaca = criarCampoTexto();
        mainPanel.add(campoPlaca, gbc);
        
        // Descrição do Problema
        gbc.gridy = 4;
        mainPanel.add(criarLabel("Descrição do problema:"), gbc);
        
        gbc.gridy = 5;
        campoDescricao = new JTextArea(4, 20);
        campoDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoDescricao.setBackground(Color.WHITE);
        campoDescricao.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        
        // Adicionar placeholder
        adicionarPlaceholderTextArea(campoDescricao, "Barulho estranho ao frear, Revisão de 10 mil km.");
        
        // Aplicar bordas arredondadas
        campoDescricao.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 5;" +
            "borderWidth: 1;");
        
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        scrollDescricao.setBorder(null);
        scrollDescricao.setOpaque(false);
        scrollDescricao.getViewport().setOpaque(false);
        scrollDescricao.setPreferredSize(new Dimension(0, 100));
        scrollDescricao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        mainPanel.add(scrollDescricao, gbc);
        
        // Data Desejada
        gbc.gridy = 6;
        mainPanel.add(criarLabel("Data desejada:"), gbc);
        
        gbc.gridy = 7;
        JPanel painelData = new JPanel(new BorderLayout());
        painelData.setOpaque(false);
        
        try {
            MaskFormatter dataFormatter = new MaskFormatter("##/##/####");
            dataFormatter.setPlaceholderCharacter('_');
            campoData = new JFormattedTextField(dataFormatter);
        } catch (ParseException e) {
            campoData = new JFormattedTextField();
        }
        
        campoData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoData.setBackground(Color.WHITE);
        campoData.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        campoData.setPreferredSize(new Dimension(0, 35));
        campoData.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        // Aplicar bordas arredondadas
        campoData.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 5;" +
            "borderWidth: 1;");
        
        // Ícone de calendário
        JLabel calendarioIcon = new JLabel("📅");
        calendarioIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        calendarioIcon.setBorder(new EmptyBorder(0, 10, 0, 10));
        calendarioIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        painelData.add(campoData, BorderLayout.CENTER);
        painelData.add(calendarioIcon, BorderLayout.EAST);
        
        mainPanel.add(painelData, gbc);
        
        // === BOTÕES ===
        gbc.gridy = 8;
        gbc.insets = new Insets(30, 0, 10, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setOpaque(false);
        
        // Botão Solicitar (verde)
        JButton btnSolicitar = new JButton("Solicitar");
        btnSolicitar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSolicitar.setForeground(Color.WHITE);
        btnSolicitar.setBackground(new Color(0x00A859)); // Verde
        btnSolicitar.setBorderPainted(false);
        btnSolicitar.setFocusPainted(false);
        btnSolicitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSolicitar.setPreferredSize(new Dimension(120, 40));
        
        btnSolicitar.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" +
            "borderWidth: 0;");
        
        btnSolicitar.addActionListener(e -> {
            if (validarCampos()) {
                JOptionPane.showMessageDialog(this, "Solicitação de manutenção enviada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        
        // Botão Cancelar (vermelho)
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(0xFF4444)); // Vermelho
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(120, 40));
        
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" +
            "borderWidth: 0;");
        
        btnCancelar.addActionListener(e -> dispose());
        
        painelBotoes.add(btnSolicitar);
        painelBotoes.add(btnCancelar);
        
        mainPanel.add(painelBotoes, gbc);
        
        // Adicionar painel principal ao dialog
        add(mainPanel);
    }
    
    /**
     * Cria um label para os campos do formulário
     */
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }
    
    /**
     * Cria um campo de texto estilizado
     */
    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBackground(Color.WHITE);
        campo.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        campo.setPreferredSize(new Dimension(0, 35));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        // Aplicar bordas arredondadas
        campo.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 5;" +
            "borderWidth: 1;");
        
        return campo;
    }
    
    /**
     * Adiciona placeholder a um JTextArea
     */
    private void adicionarPlaceholderTextArea(JTextArea textArea, String placeholder) {
        textArea.setText(placeholder);
        textArea.setForeground(new Color(150, 150, 150));
        
        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(new Color(60, 60, 60));
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textArea.getText().trim().isEmpty()) {
                    textArea.setText(placeholder);
                    textArea.setForeground(new Color(150, 150, 150));
                }
            }
        });
    }
    
    /**
     * Valida os campos do formulário
     */
    private boolean validarCampos() {
        if (campoPlaca.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha a placa do veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoPlaca.requestFocus();
            return false;
        }
        
        String descricao = campoDescricao.getText().trim();
        if (descricao.isEmpty() || descricao.equals("Barulho estranho ao frear, Revisão de 10 mil km.")) {
            JOptionPane.showMessageDialog(this, "Por favor, descreva o problema.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoDescricao.requestFocus();
            return false;
        }
        
        String data = campoData.getText().trim();
        if (data.isEmpty() || data.contains("_")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma data desejada.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoData.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Método para exibir a tela de solicitação de manutenção
     */
    public static void mostrar(JFrame parent) {
        SwingUtilities.invokeLater(() -> {
            SolicitarManutencaoTela tela = new SolicitarManutencaoTela(parent);
            tela.setVisible(true);
        });
    }
}

