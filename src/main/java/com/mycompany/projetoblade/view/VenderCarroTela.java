package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

/**
 * Tela de venda de carro - Modal com design moderno
 */
public class VenderCarroTela extends JDialog {
    
    private JComboBox<String> campoMarca;
    private JTextField campoModelo;
    private JTextField campoAno;
    private JFormattedTextField campoPlaca;
    private JTextField campoQuilometragem;
    private JTextField campoPreco;
    private JLabel labelFotos;
    private int fotosSelecionadas = 0;
    
    private com.mycompany.projetoblade.service.VeiculoService veiculoService;
    private com.mycompany.projetoblade.service.ClienteService clienteService;

    public VenderCarroTela(JFrame parent, com.mycompany.projetoblade.service.VeiculoService veiculoService, com.mycompany.projetoblade.service.ClienteService clienteService) {
        super(parent, true); // Modal
        setUndecorated(true); // Remove barra de título padrão
        setSize(550, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Fundo cinza claro
        getContentPane().setBackground(new Color(0xE0E0E0));
        
        // Aplicar bordas arredondadas na janela
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 550, 700, 15, 15));
        
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(0xE0E0E0));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // === TOPO ===
        // Painel do topo com botão X
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
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
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Título "Vender meu Veículo"
        JLabel titulo = new JLabel("Vender meu Veículo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // === PAINEL CENTRAL COM CAMPOS ===
        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new BoxLayout(painelCampos, BoxLayout.Y_AXIS));
        painelCampos.setBackground(new Color(0xF5F5F5)); // Cinza claro para contraste
        painelCampos.setBorder(new EmptyBorder(25, 25, 25, 25));
        painelCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelCampos.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        // Marca (JComboBox)
        painelCampos.add(criarLabel("Marca:"));
        painelCampos.add(Box.createVerticalStrut(5));
        String[] marcas = {"Selecione uma marca", "Volkswagen", "Ford", "Chevrolet", "Fiat", "Toyota", "Honda", "Hyundai", "Nissan", "Renault", "Peugeot", "Citroën", "Jeep", "Dodge", "BMW", "Mercedes-Benz", "Audi", "Outra"};
        campoMarca = new JComboBox<>(marcas);
        estilizarCampo(campoMarca);
        painelCampos.add(campoMarca);
        painelCampos.add(Box.createVerticalStrut(15));
        
        // Modelo
        painelCampos.add(criarLabel("Modelo:"));
        painelCampos.add(Box.createVerticalStrut(5));
        campoModelo = criarCampoTexto();
        painelCampos.add(campoModelo);
        painelCampos.add(Box.createVerticalStrut(15));
        
        // Ano
        painelCampos.add(criarLabel("Ano:"));
        painelCampos.add(Box.createVerticalStrut(5));
        campoAno = criarCampoTexto();
        painelCampos.add(campoAno);
        painelCampos.add(Box.createVerticalStrut(15));
        
        // Placa
        painelCampos.add(criarLabel("Placa:"));
        painelCampos.add(Box.createVerticalStrut(5));
        try {
            MaskFormatter placaMask = new MaskFormatter("UUU-####");
            placaMask.setPlaceholderCharacter('_');
            campoPlaca = new JFormattedTextField(placaMask);
            campoPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            campoPlaca.setBackground(Color.WHITE);
            campoPlaca.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
            campoPlaca.setPreferredSize(new Dimension(0, 35));
            campoPlaca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            campoPlaca.setAlignmentX(Component.LEFT_ALIGNMENT);
            campoPlaca.putClientProperty(FlatClientProperties.STYLE, 
                "arc: 5;" +
                "borderWidth: 1;");
        } catch (ParseException ex) {
            // Fallback para campo de texto comum
            campoPlaca = new JFormattedTextField();
            campoPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            campoPlaca.setBackground(Color.WHITE);
            campoPlaca.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
            campoPlaca.setPreferredSize(new Dimension(0, 35));
            campoPlaca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            campoPlaca.setAlignmentX(Component.LEFT_ALIGNMENT);
            campoPlaca.putClientProperty(FlatClientProperties.STYLE, 
                "arc: 5;" +
                "borderWidth: 1;");
        }
        painelCampos.add(campoPlaca);
        painelCampos.add(Box.createVerticalStrut(15));
        
        // Quilometragem
        painelCampos.add(criarLabel("Quilometragem:"));
        painelCampos.add(Box.createVerticalStrut(5));
        campoQuilometragem = criarCampoTexto();
        painelCampos.add(campoQuilometragem);
        painelCampos.add(Box.createVerticalStrut(15));
        
        // Preço Desejado
        painelCampos.add(criarLabel("Preço Desejado:"));
        painelCampos.add(Box.createVerticalStrut(5));
        campoPreco = criarCampoTexto();
        painelCampos.add(campoPreco);
        painelCampos.add(Box.createVerticalStrut(20));
        
        // Fotos
        JPanel painelFotos = new JPanel(new BorderLayout());
        painelFotos.setOpaque(false);
        painelFotos.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnAdicionarFotos = new JButton("Adicionar Fotos");
        btnAdicionarFotos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdicionarFotos.setForeground(Color.WHITE);
        btnAdicionarFotos.setBackground(new Color(0x4A90E2)); // Azul
        btnAdicionarFotos.setBorderPainted(false);
        btnAdicionarFotos.setFocusPainted(false);
        btnAdicionarFotos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionarFotos.setPreferredSize(new Dimension(150, 40));
        btnAdicionarFotos.setMaximumSize(new Dimension(150, 40));
        
        btnAdicionarFotos.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" +
            "borderWidth: 0;");
        
        btnAdicionarFotos.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imagens", "jpg", "jpeg", "png", "gif", "bmp"));
            
            int resultado = fileChooser.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File[] arquivos = fileChooser.getSelectedFiles();
                fotosSelecionadas = arquivos.length;
                if (fotosSelecionadas > 0) {
                    labelFotos.setText(fotosSelecionadas + " foto(s) selecionada(s)");
                    labelFotos.setForeground(new Color(0x00A859)); // Verde
                } else {
                    labelFotos.setText("Nenhuma foto selecionada");
                    labelFotos.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        labelFotos = new JLabel("Nenhuma foto selecionada");
        labelFotos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelFotos.setForeground(new Color(150, 150, 150));
        labelFotos.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        painelFotos.add(btnAdicionarFotos, BorderLayout.WEST);
        painelFotos.add(labelFotos, BorderLayout.CENTER);
        
        painelCampos.add(painelFotos);
        
        mainPanel.add(painelCampos);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // === RODAPÉ (BOTÕES) ===
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setOpaque(false);
        painelBotoes.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botão "Enviar para Avaliação" (verde)
        JButton btnEnviar = new JButton("Enviar para Avaliação");
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setBackground(new Color(0x00A859)); // Verde
        btnEnviar.setBorderPainted(false);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.setPreferredSize(new Dimension(180, 45));
        
        btnEnviar.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" +
            "borderWidth: 0;");
        
        this.veiculoService = veiculoService;
        this.clienteService = clienteService;

        btnEnviar.addActionListener(e -> {
            if (!validarCampos()) return;

            // Require login before allowing vehicle registration
            if (!com.mycompany.projetoblade.utils.Sessao.isLogado()) {
                int opt = JOptionPane.showConfirmDialog(this, "Você precisa estar logado para cadastrar um veículo. Deseja fazer login agora?", "Login necessário", JOptionPane.YES_NO_OPTION);
                    if (opt == JOptionPane.YES_OPTION) {
                        try {
                            LoginTela.mostrar(parent, this.clienteService);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Erro ao abrir a tela de login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                if (!com.mycompany.projetoblade.utils.Sessao.isLogado()) {
                    JOptionPane.showMessageDialog(this, "Cadastro cancelado. Faça login para cadastrar um veículo.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            try {
                String marca = campoMarca.getSelectedItem().toString();
                String modelo = campoModelo.getText().trim();
                int ano = Integer.parseInt(campoAno.getText().trim());
                String placa = campoPlaca.getText().trim().toUpperCase();
                // gera chassi robusto a partir de UUID (12 caracteres hex)
                String chassi = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
                double preco = Double.parseDouble(campoPreco.getText().trim().replace(",", "."));

                // Verifica se já existe veículo com a placa informada
                if (this.veiculoService != null) {
                    try {
                        java.util.Optional<com.mycompany.projetoblade.model.Veiculo> existente = this.veiculoService.buscarPorPlaca(placa);
                        if (existente != null && existente.isPresent()) {
                            JOptionPane.showMessageDialog(this, "Já existe um veículo cadastrado com esta placa.", "Atenção", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } catch (IllegalArgumentException iae) {
                        // buscarPorPlaca pode lançar se a placa for inválida; ignore aqui porque validamos o campo
                    }
                }

                com.mycompany.projetoblade.model.Veiculo novo = new com.mycompany.projetoblade.model.Veiculo(modelo, marca, ano, placa, chassi);
                novo.setPreco(preco);
                // set owner if logged
                if (com.mycompany.projetoblade.utils.Sessao.getClienteLogado() != null) {
                    novo.setDono(com.mycompany.projetoblade.utils.Sessao.getClienteLogado());
                }

                // Ensure status is DISPONIVEL (constructor already sets it, but enforce)
                novo.setStatus("DISPONIVEL");

                // Persist via service and handle validation errors gracefully
                try {
                    if (this.veiculoService != null) {
                        this.veiculoService.salvarVeiculo(novo);
                    }

                    JOptionPane.showMessageDialog(
                        this,
                        "Seu veículo foi cadastrado e está disponível em Ofertas!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Não foi possível cadastrar o veículo: " + ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar veículo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Botão "Cancelar"
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setForeground(new Color(60, 60, 60));
        btnCancelar.setBackground(new Color(0xE0E0E0));
        btnCancelar.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(120, 45));
        
        btnCancelar.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" +
            "borderWidth: 1;");
        
        btnCancelar.addActionListener(e -> dispose());
        
        painelBotoes.add(btnEnviar);
        painelBotoes.add(btnCancelar);
        
        mainPanel.add(painelBotoes);
        
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
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Aplicar bordas arredondadas
        campo.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 5;" +
            "borderWidth: 1;");
        
        return campo;
    }
    
    /**
     * Estiliza um JComboBox
     */
    private void estilizarCampo(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        combo.setPreferredSize(new Dimension(0, 35));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Aplicar bordas arredondadas
        combo.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 5;" +
            "borderWidth: 1;");
    }
    
    /**
     * Valida os campos do formulário
     */
    private boolean validarCampos() {
        if (campoMarca.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione a marca do veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoMarca.requestFocus();
            return false;
        }
        
        if (campoModelo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o modelo do veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoModelo.requestFocus();
            return false;
        }
        
        if (campoAno.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o ano do veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoAno.requestFocus();
            return false;
        }
        
        try {
            int ano = Integer.parseInt(campoAno.getText().trim());
            if (ano < 1900 || ano > java.time.Year.now().getValue() + 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um ano válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoAno.requestFocus();
            return false;
        }
        
        String placaText = campoPlaca.getText().trim().toUpperCase();
        if (placaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha a placa do veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoPlaca.requestFocus();
            return false;
        }
        // Valida formato AAA-1234
        if (!placaText.matches("[A-Z]{3}-\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Placa inválida. Use o formato AAA-1234 (ex: ABC-1234).", "Erro", JOptionPane.ERROR_MESSAGE);
            campoPlaca.requestFocus();
            return false;
        }
        
        if (campoQuilometragem.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha a quilometragem do veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoQuilometragem.requestFocus();
            return false;
        }
        
        try {
            int km = Integer.parseInt(campoQuilometragem.getText().trim());
            if (km < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira uma quilometragem válida.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoQuilometragem.requestFocus();
            return false;
        }
        
        if (campoPreco.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o preço desejado.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoPreco.requestFocus();
            return false;
        }
        
        try {
            double preco = Double.parseDouble(campoPreco.getText().trim().replace(",", "."));
            if (preco <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um preço válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoPreco.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Método para exibir a tela de venda de carro
     */
    public static void mostrar(JFrame parent) {
        SwingUtilities.invokeLater(() -> {
            VenderCarroTela tela = new VenderCarroTela(parent, null, null);
            tela.setVisible(true);
        });
    }

    public static void mostrar(JFrame parent, com.mycompany.projetoblade.service.VeiculoService veiculoService, com.mycompany.projetoblade.service.ClienteService clienteService) {
        SwingUtilities.invokeLater(() -> {
            VenderCarroTela tela = new VenderCarroTela(parent, veiculoService, clienteService);
            tela.setVisible(true);
        });
    }
}

