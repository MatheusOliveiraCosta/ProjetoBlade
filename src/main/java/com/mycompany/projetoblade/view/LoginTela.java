package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.service.ClienteService;
import com.mycompany.projetoblade.utils.Sessao;
import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Mecanico;
import com.mycompany.projetoblade.model.Administrador;
import com.mycompany.projetoblade.model.Usuario;
import java.time.LocalDate;
import com.mycompany.projetoblade.view.AgendaOficinaTela;
import com.mycompany.projetoblade.view.TelaGerenciarClientes;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Tela de login - Modal com design moderno
 */
public class LoginTela extends JDialog {
    
    private JTextField campoNome;
    private JPasswordField campoSenha;
    private ClienteService clienteService;
    
    public LoginTela(JFrame parent, ClienteService clienteService) {
        super(parent, true); // Modal
        this.clienteService = clienteService;
        setUndecorated(true); // Remove barra de título padrão
        setSize(450, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Fundo cinza claro
        getContentPane().setBackground(new Color(0xE0E0E0));
        
        // Painel principal com bordas arredondadas
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(0xE0E0E0));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // === TOPO ===
        // Painel do topo com logo e botão X
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
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
        
        // Logo centralizado
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        try {
            java.net.URL logoUrl = getClass().getResource("/images/logo.png");
            if (logoUrl == null) {
                logoUrl = getClass().getClassLoader().getResource("images/logo.png");
            }
            
            if (logoUrl != null) {
                java.awt.image.BufferedImage imgLogo = javax.imageio.ImageIO.read(logoUrl);
                int targetHeight = 60;
                double ratio = (double) imgLogo.getWidth() / imgLogo.getHeight();
                int targetWidth = (int) (targetHeight * ratio);
                Image scaledLogo = imgLogo.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledLogo));
            } else {
                // Fallback: texto do logo
                logoLabel.setText("BLADE MOTORS");
                logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
                logoLabel.setForeground(Color.BLACK);
            }
        } catch (Exception e) {
            logoLabel.setText("BLADE MOTORS");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            logoLabel.setForeground(Color.BLACK);
        }
        
        topPanel.add(logoLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Título "LOGIN"
        JLabel titulo = new JLabel("LOGIN");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // === CAMPOS DO FORMULÁRIO ===
        // Email (usado para autenticação)
        campoNome = new JTextField();
        adicionarPlaceholder(campoNome, "email@exemplo.com");
        mainPanel.add(criarCampoFormulario("Email:", campoNome));
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Senha
        campoSenha = new JPasswordField();
        adicionarPlaceholder(campoSenha, "Password");
        mainPanel.add(criarCampoFormulario("Senha:", campoSenha));
        mainPanel.add(Box.createVerticalStrut(25));
        
        // === RODAPÉ ===
        // Texto "Não possui conta? Clique aqui para se cadastrar"
        JLabel textoCadastro = new JLabel("<html><body style='text-align: center;'>Não possui conta? <a href='#' style='color: #0066CC; text-decoration: none;'>Clique aqui para se cadastrar</a></body></html>");
        textoCadastro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textoCadastro.setAlignmentX(Component.CENTER_ALIGNMENT);
        textoCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Adicionar listener para o link
        textoCadastro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Fecha a tela de login e abre a tela de cadastro
                dispose();
                CadastroClienteTela.mostrar(parent, LoginTela.this.clienteService);
            }
        });
        
        mainPanel.add(textoCadastro);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Botão "Entrar" (verde)
        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEntrar.setForeground(Color.BLACK); // Texto preto conforme a imagem
        btnEntrar.setBackground(new Color(0x00A859)); // Verde
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setPreferredSize(new Dimension(0, 45));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Aplicar bordas arredondadas usando FlatLaf
        btnEntrar.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 10;" + // Bordas arredondadas
            "borderWidth: 0;");
        
        // Ação do botão - autentica com ClienteService e registra Sessão
        btnEntrar.addActionListener(e -> {
            if (validarCampos()) {
                String email = campoNome.getText().trim();
                String senha = new String(campoSenha.getPassword());

                try {
                    // Special test accounts: mecanico and administrador
                    if ("MEC@gmail.com".equalsIgnoreCase(email) && "123456".equals(senha)) {
                        Usuario u = new Usuario("Mecânico Test", "MEC@gmail.com", "123456");
                        Mecanico mec = new Mecanico("M001", LocalDate.now(), u, "Geral");
                        Sessao.login(mec);
                        JOptionPane.showMessageDialog(this, "Login como Mecânico realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        // Open agenda for mecânico
                        JFrame owner = (JFrame) this.getOwner();
                        SwingUtilities.invokeLater(() -> AgendaOficinaTela.mostrar(owner, new com.mycompany.projetoblade.service.ManutencaoService(new com.mycompany.projetoblade.repository.ManutencaoRepositoryImpl())));
                        return;
                    }

                    if ("ADM@gmail.com".equalsIgnoreCase(email) && "123456".equals(senha)) {
                        Usuario u = new Usuario("Administrador Test", "ADM@gmail.com", "123456");
                        Administrador adm = new Administrador("A001", LocalDate.now(), u, "TOTAL");
                        Sessao.login(adm);
                        JOptionPane.showMessageDialog(this, "Login como Administrador realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        // Open client management for administrador
                        SwingUtilities.invokeLater(() -> {
                            TelaGerenciarClientes tela = new TelaGerenciarClientes((JFrame) this.getOwner(), new com.mycompany.projetoblade.service.ClienteService(new com.mycompany.projetoblade.repository.ClienteRepositoryImpl()));
                            tela.setVisible(true);
                        });
                        return;
                    }

                    // Fallback to existing ClienteService authentication
                    Cliente cliente = this.clienteService.autenticar(email, senha);
                    if (cliente != null) {
                        Sessao.login(cliente);
                        JOptionPane.showMessageDialog(this, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Credenciais inválidas. Verifique o e-mail e a senha.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao tentar autenticar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        mainPanel.add(btnEntrar);
        
        // Adicionar painel principal ao dialog
        add(mainPanel);
    }
    
    /**
     * Cria um campo de formulário com label e campo de texto
     */
    private JPanel criarCampoFormulario(String labelTexto, JComponent campo) {
        JPanel painelCampo = new JPanel();
        painelCampo.setLayout(new BoxLayout(painelCampo, BoxLayout.Y_AXIS));
        painelCampo.setOpaque(false);
        painelCampo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCampo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Label
        JLabel label = new JLabel(labelTexto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(60, 60, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCampo.add(label);
        painelCampo.add(Box.createVerticalStrut(5));
        
        // Campo de texto
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBackground(Color.WHITE);
        campo.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        campo.setPreferredSize(new Dimension(0, 35));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        // Aplicar bordas arredondadas usando FlatLaf
        campo.putClientProperty(FlatClientProperties.STYLE, 
            "arc: 5;" + // Bordas arredondadas suaves
            "borderWidth: 1;");
        
        painelCampo.add(campo);
        
        return painelCampo;
    }
    
    /**
     * Adiciona placeholder a um campo de texto
     */
    private void adicionarPlaceholder(JComponent campo, String placeholder) {
        if (campo instanceof JTextField) {
            JTextField textField = (JTextField) campo;
            textField.setText(placeholder);
            textField.setForeground(new Color(150, 150, 150));
            
            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (textField.getText().equals(placeholder)) {
                        textField.setText("");
                        textField.setForeground(new Color(60, 60, 60));
                    }
                }
                
                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (textField.getText().isEmpty()) {
                        textField.setText(placeholder);
                        textField.setForeground(new Color(150, 150, 150));
                    }
                }
            });
        } else if (campo instanceof JPasswordField) {
            JPasswordField passwordField = (JPasswordField) campo;
            passwordField.setEchoChar((char) 0); // Mostra texto normal temporariamente
            passwordField.setText(placeholder);
            passwordField.setForeground(new Color(150, 150, 150));
            
            passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (new String(passwordField.getPassword()).equals(placeholder)) {
                        passwordField.setText("");
                        passwordField.setEchoChar('●'); // Volta a mostrar como senha
                        passwordField.setForeground(new Color(60, 60, 60));
                    }
                }
                
                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (passwordField.getPassword().length == 0) {
                        passwordField.setEchoChar((char) 0);
                        passwordField.setText(placeholder);
                        passwordField.setForeground(new Color(150, 150, 150));
                    }
                }
            });
        }
    }
    
    /**
     * Valida os campos do formulário
     */
    private boolean validarCampos() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty() || nome.equals("João da Silva")) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o nome completo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoNome.requestFocus();
            return false;
        }
        
        String senha = new String(campoSenha.getPassword());
        if (senha.isEmpty() || senha.equals("Password")) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha a senha.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoSenha.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Método para exibir a tela de login
     */
    public static void mostrar(JFrame parent, ClienteService clienteService) {
        SwingUtilities.invokeLater(() -> {
            LoginTela tela = new LoginTela(parent, clienteService);
            tela.setVisible(true);
        });
    }
}

