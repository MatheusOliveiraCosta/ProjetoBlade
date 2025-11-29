package com.mycompany.projetoblade.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.service.ClienteService;
import com.mycompany.projetoblade.utils.Sessao;

/**
 * Tela de cadastro de cliente - Modal com design moderno
 */
public class CadastroClienteTela extends JDialog {
    
    private JTextField campoNome;
    private JPasswordField campoSenha;
    private JPasswordField campoRepetirSenha;
    private JFormattedTextField campoCPF;
    private JTextField campoCelular;
    private JTextField campoEmail;
    private JTextField campoEndereco;
    private ClienteService clienteService;
    private Cliente clienteEditando; // Cliente sendo editado (null se for novo)
    private JLabel titulo; // Referência ao título para mudar entre "CADASTRAR" e "EDITAR"
    private JButton btnEntrar; // Referência ao botão para mudar texto
    
    public CadastroClienteTela(JFrame parent, ClienteService clienteService) {
        this(parent, clienteService, null);
    }
    
    public CadastroClienteTela(JFrame parent, ClienteService clienteService, Cliente clienteEditando) {
        super(parent, true); // Modal
        this.clienteService = clienteService;
        this.clienteEditando = clienteEditando;
        setUndecorated(true); // Remove barra de título padrão
        setSize(450, 700);
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
        
        // Título "CADASTRAR" ou "EDITAR"
        titulo = new JLabel(clienteEditando != null ? "EDITAR" : "CADASTRAR");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // === CAMPOS DO FORMULÁRIO ===
        // Nome completo
        mainPanel.add(criarCampoFormulario("Nome completo:", campoNome = new JTextField()));
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Criar senha
        mainPanel.add(criarCampoFormulario("Criar senha:", campoSenha = new JPasswordField()));
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Repetir senha
        mainPanel.add(criarCampoFormulario("Repetir a senha:", campoRepetirSenha = new JPasswordField()));
        mainPanel.add(Box.createVerticalStrut(15));
        
        // CPF com máscara
        try {
            MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
            cpfFormatter.setPlaceholderCharacter('_');
            campoCPF = new JFormattedTextField(cpfFormatter);
        } catch (ParseException e) {
            campoCPF = new JFormattedTextField();
        }
        mainPanel.add(criarCampoFormulario("CPF:", campoCPF));
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Celular
        mainPanel.add(criarCampoFormulario("Celular:", campoCelular = new JTextField()));
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Email
        mainPanel.add(criarCampoFormulario("Email:", campoEmail = new JTextField()));
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Endereço
        mainPanel.add(criarCampoFormulario("Endereço:", campoEndereco = new JTextField()));
        mainPanel.add(Box.createVerticalStrut(25));
        
        // === RODAPÉ ===
        // Texto "Já possui conta? Clique aqui para logar"
        JLabel textoLogin = new JLabel("<html><body style='text-align: center;'>Já possui conta? <a href='#' style='color: #0066CC; text-decoration: none;'>Clique aqui para logar</a></body></html>");
        textoLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textoLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        textoLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Adicionar listener para o link
        textoLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Fecha a tela de cadastro e abre a tela de login
                dispose();
                try {
                    LoginTela.mostrar(parent, CadastroClienteTela.this.clienteService);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(parent, "Erro ao abrir a tela de login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Só mostra o link de login se não estiver editando
        if (clienteEditando == null) {
            mainPanel.add(textoLogin);
            mainPanel.add(Box.createVerticalStrut(20));
        } else {
            mainPanel.add(Box.createVerticalStrut(20));
        }
        
        // Botão "Entrar" ou "Salvar" (verde)
        btnEntrar = new JButton(clienteEditando != null ? "SALVAR" : "ENTRAR");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEntrar.setForeground(Color.WHITE);
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
        
        // Ação do botão
        btnEntrar.addActionListener(e -> {
            if (validarCampos()) {
                try {
                    String senha = new String(campoSenha.getPassword());
                    // Se estiver editando e a senha estiver vazia, mantém a senha antiga
                    String senhaFinal = senha;
                    if (clienteEditando != null && senha.isEmpty()) {
                        senhaFinal = clienteEditando.getUsuario().getSenha();
                    }
                    
                    Usuario usuario = new Usuario(campoNome.getText().trim(), campoEmail.getText().trim(), senhaFinal);
                    Cliente cliente = new Cliente(campoEndereco.getText().trim(), campoCPF.getText().trim(), campoCelular.getText().trim(), usuario);
                    
                    // Se estiver editando, mantém o ID
                    if (clienteEditando != null) {
                        cliente.setId(clienteEditando.getId());
                    }

                    Cliente salvo = this.clienteService.salvarCliente(cliente);
                    
                    // Só faz login automático se for novo cadastro
                    if (clienteEditando == null) {
                        Sessao.login(salvo);
                    }

                    JOptionPane.showMessageDialog(this, 
                        clienteEditando != null ? "Cliente atualizado com sucesso!" : "Cadastro realizado com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Se estiver editando, preenche os campos com os dados do cliente
        if (clienteEditando != null) {
            preencherCampos(clienteEditando);
        }
        
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
     * Preenche os campos com os dados do cliente para edição
     */
    private void preencherCampos(Cliente cliente) {
        if (cliente.getUsuario() != null) {
            campoNome.setText(cliente.getUsuario().getNome());
            campoEmail.setText(cliente.getUsuario().getEmail());
            // Não preenche a senha por segurança
            campoSenha.setText("");
            campoRepetirSenha.setText("");
        }
        if (cliente.getCpf() != null) {
            campoCPF.setText(cliente.getCpf());
        }
        if (cliente.getCelular() != null) {
            campoCelular.setText(cliente.getCelular());
        }
        if (cliente.getEndereco() != null) {
            campoEndereco.setText(cliente.getEndereco());
        }
    }
    
    /**
     * Valida os campos do formulário (versão para edição permite senha vazia)
     */
    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o nome completo.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoNome.requestFocus();
            return false;
        }
        
        String senha = new String(campoSenha.getPassword());
        String repetirSenha = new String(campoRepetirSenha.getPassword());
        
        // Se estiver editando, senha é opcional
        if (clienteEditando == null) {
            if (senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, crie uma senha.", "Erro", JOptionPane.ERROR_MESSAGE);
                campoSenha.requestFocus();
                return false;
            }
            
            if (!senha.equals(repetirSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
                campoRepetirSenha.requestFocus();
                return false;
            }
        } else {
            // Se estiver editando e preencheu senha, valida
            if (!senha.isEmpty() && !senha.equals(repetirSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
                campoRepetirSenha.requestFocus();
                return false;
            }
        }
        
        if (campoCelular.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o celular.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoCelular.requestFocus();
            return false;
        }

        if (campoEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o email.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoEmail.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Método para exibir a tela de cadastro
     */
    public static void mostrar(JFrame parent, ClienteService clienteService) {
        SwingUtilities.invokeLater(() -> {
            CadastroClienteTela tela = new CadastroClienteTela(parent, clienteService);
            tela.setVisible(true);
        });
    }
    
    /**
     * Método para exibir a tela de edição de cliente
     */
    public static void editar(JFrame parent, ClienteService clienteService, Cliente cliente) {
        SwingUtilities.invokeLater(() -> {
            CadastroClienteTela tela = new CadastroClienteTela(parent, clienteService, cliente);
            tela.setVisible(true);
        });
    }
}

