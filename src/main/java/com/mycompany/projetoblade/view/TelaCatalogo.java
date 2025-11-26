package com.mycompany.projetoblade.view;

import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.model.Cliente;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;

/**
 * Tela de catálogo de veículos - Interface moderna inspirada no site Blade Motors.
 * Replica fielmente a interface de venda de carros com todas as seções.
 */
public class TelaCatalogo extends JPanel {
    
    private JPanel painelOfertas;
    private JTextField campoPesquisa;
    private List<Veiculo> veiculos;
    private List<CardVeiculo> cardsVeiculos;
    private JFrame parentFrame;
    private Cliente clienteLogado; // Cliente atualmente logado
    
    public TelaCatalogo(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.veiculos = new ArrayList<>();
        this.cardsVeiculos = new ArrayList<>();
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Painel principal com scroll
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Painel de conteúdo
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(Color.WHITE);
        
        // Adicionar seções
        conteudo.add(criarHeader());
        conteudo.add(criarBannerImagem());
        conteudo.add(criarBarraPesquisa());
        conteudo.add(criarSecaoMaisPopulares());
        conteudo.add(criarSecaoOfertas());
        conteudo.add(criarFooter());
        
        scrollPane.setViewportView(conteudo);
        add(scrollPane, BorderLayout.CENTER);
        
        // Popular veículos
        populaVeiculosFake();
        atualizarGridOfertas();
    }
    
    /**
     * Cria o header com logo e navegação
     */
    /**
     * Cria o header com LOGO (Imagem) e navegação
     */
    private JPanel criarHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(Color.WHITE);
        // Margem direita de 60px para segurança
        header.setBorder(new EmptyBorder(10, 40, 10, 60)); 
        header.setPreferredSize(new Dimension(0, 80));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // --- 1. ESQUERDA (Botão Meus Veículos) ---
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(Color.WHITE);
        JButton btnMeusVeiculos = new JButton("Meus veículos");
        btnMeusVeiculos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnMeusVeiculos.setForeground(new Color(60, 60, 60));
        btnMeusVeiculos.setBorderPainted(false);
        btnMeusVeiculos.setContentAreaFilled(false);
        btnMeusVeiculos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leftPanel.add(btnMeusVeiculos);
        btnMeusVeiculos.addActionListener(e -> {
            // Verifica se há cliente logado
            if (clienteLogado == null) {
                JOptionPane.showMessageDialog(this, "Você precisa estar logado para acessar seus veículos.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Busca o veículo do cliente (apenas 1 veículo por cliente)
            Veiculo veiculoCliente = buscarVeiculoDoCliente(clienteLogado);
            
            if (veiculoCliente == null) {
                JOptionPane.showMessageDialog(this, "Você ainda não possui um veículo.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Abre a tela com o único veículo
                MeusVeiculosTela.mostrar(parentFrame, veiculoCliente);
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        header.add(leftPanel, gbc);
        
        // --- 2. CENTRO (Logo) ---
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            java.net.URL logoUrl = getClass().getResource("/images/logo.png");
            if (logoUrl == null) logoUrl = getClass().getClassLoader().getResource("images/logo.png");
            if (logoUrl != null) {
                BufferedImage imgLogo = ImageIO.read(logoUrl);
                int targetHeight = 55; 
                double ratio = (double) imgLogo.getWidth() / imgLogo.getHeight();
                int targetWidth = (int) (targetHeight * ratio);
                Image scaledLogo = imgLogo.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledLogo));
            } else { logoLabel.setText("BLADE MOTORS"); }
        } catch (Exception e) { logoLabel.setText("BLADE MOTORS"); }

        gbc.gridx = 1; gbc.weightx = 0.4; gbc.anchor = GridBagConstraints.CENTER;
        header.add(logoLabel, gbc);
        
        // --- 3. DIREITA (Navegação + Ícone User) ---
        // Usando FlowLayout.LEFT para que a ordem de adição corresponda à ordem visual
        // e depois alinhando o painel à direita com GridBagConstraints
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(700, 80)); 
        
        // Botão Conserto
        JButton btnConserto = new JButton("Conserto");
        btnConserto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnConserto.setForeground(new Color(60, 60, 60));
        btnConserto.setMargin(new Insets(0, 0, 0, 0));
        btnConserto.setBorderPainted(false);
        btnConserto.setContentAreaFilled(false);
        btnConserto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(btnConserto);
        btnConserto.addActionListener(e -> {
            // Cria e mostra a tela como um modal sobre a janela principal
            SolicitarManutencaoTela telaManutencao = new SolicitarManutencaoTela(parentFrame);
            telaManutencao.setVisible(true);
        });
        
        // Botão Vender
        JButton btnVender = new JButton("Vender");
        btnVender.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVender.setForeground(new Color(60, 60, 60));
        btnVender.setMargin(new Insets(0, 0, 0, 0));
        btnVender.setBorderPainted(false);
        btnVender.setContentAreaFilled(false);
        btnVender.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(btnVender);
        btnVender.addActionListener(e -> {
            // Abre a tela para o cliente cadastrar o carro dele
            // Certifique-se de que o Cursor já criou a classe VenderCarroTela antes!
            try {
                VenderCarroTela telaVenda = new VenderCarroTela(parentFrame);
                telaVenda.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento!");
            }
        });
        
        // --- ÍCONE DE USUÁRIO (logo após Vender) ---
        JButton btnUserIcon = new JButton();
        btnUserIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUserIcon.setBorderPainted(false);
        btnUserIcon.setContentAreaFilled(false);
        btnUserIcon.setFocusPainted(false);
        btnUserIcon.setMargin(new Insets(0, 0, 0, 0));

        try {
            // Carrega user.png
            java.net.URL userUrl = getClass().getResource("/images/user.png");
            if (userUrl == null) userUrl = getClass().getClassLoader().getResource("images/user.png");
            
            if (userUrl != null) {
                BufferedImage imgUser = ImageIO.read(userUrl);
                // Redimensiona para 32x32
                Image scaledUser = imgUser.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                btnUserIcon.setIcon(new ImageIcon(scaledUser));
            } else {
                btnUserIcon.setText("👤");
                btnUserIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            }
        } catch (Exception e) {
            btnUserIcon.setText("👤");
            btnUserIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        }

        // Ação ao clicar no ícone - abre tela de cadastro
        btnUserIcon.addActionListener(e -> {
            CadastroClienteTela.mostrar(parentFrame);
        });

        // Adiciona o ícone logo após o botão Vender
        rightPanel.add(btnUserIcon);

        gbc.gridx = 2; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.EAST;
        header.add(rightPanel, gbc);
        
        return header;
    }
    
    
    /**
     * Cria o painel de banner com imagem centralizada
     */
    private JPanel criarBannerImagem() {
        // GridBagLayout é o melhor para centralizar um único componente
        JPanel heroPanel = new JPanel(new GridBagLayout()); 
        
        // DICA DE DESIGN: Use uma cor de fundo que combine com as bordas da sua imagem
        // (Aquele cinza do degradê da imagem original)
        heroPanel.setBackground(new Color(225, 225, 225)); 
        
        try {
            // Tentar carregar a imagem de diferentes locais
            java.net.URL imageUrl = getClass().getResource("/images/banner_hero.jpg");
            
            // Fallbacks caso o nome esteja diferente
            if (imageUrl == null) imageUrl = getClass().getResource("/images/banner_hero.png");
            if (imageUrl == null) imageUrl = getClass().getResource("/com/mycompany/projetoblade/view/imagens/banner_hero.png");
            
            if (imageUrl != null) {
                BufferedImage originalImage = ImageIO.read(imageUrl);
                
                // Configuração de redimensionamento inteligente
                // Definimos uma altura fixa para o banner (ex: 300px)
                int targetHeight = 300; 
                
                // Calcula a largura proporcional para não esticar a imagem
                double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);
                
                // Redimensiona a imagem com alta qualidade (SCALE_SMOOTH)
                Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(scaledImage);
                
                JLabel imageLabel = new JLabel(imageIcon);
                
                // Adiciona ao painel (o GridBagLayout vai centralizar automaticamente)
                heroPanel.add(imageLabel);
                
            } else {
                // Fallback visual caso a imagem não exista
                JLabel placeholder = new JLabel("Banner Promocional", SwingConstants.CENTER);
                placeholder.setFont(new Font("Segoe UI", Font.BOLD, 24));
                placeholder.setForeground(Color.GRAY);
                placeholder.setPreferredSize(new Dimension(800, 250));
                heroPanel.add(placeholder);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem do banner: " + e.getMessage());
        }
        
        return heroPanel;
    }
    
    /**
     * Cria a barra de pesquisa funcional
     */
    /**
     * Cria a barra de pesquisa funcional com botão "Mostrar todos"
     */
    private JPanel criarBarraPesquisa() {
        // Painel externo com GridBagLayout para centralizar o conteúdo
        JPanel painelExterno = new JPanel(new GridBagLayout());
        painelExterno.setBackground(Color.WHITE);
        painelExterno.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Container visual da barra (o retângulo cinza)
        // Aumentamos um pouco a largura para caber o novo botão
        JPanel searchContainer = new JPanel(new BorderLayout(10, 0)); // Gap horizontal de 10
        searchContainer.setBackground(new Color(245, 245, 245));
        searchContainer.setBorder(new EmptyBorder(10, 20, 10, 20));
        searchContainer.setPreferredSize(new Dimension(700, 50)); // Aumentei de 600 para 700px
        
        // --- ESQUERDA: Ícone Menu ---
        JLabel menuIcon = new JLabel("☰");
        menuIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // --- CENTRO: Campo de Pesquisa ---
        campoPesquisa = new JTextField();
        campoPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoPesquisa.setBorder(null);
        campoPesquisa.setBackground(new Color(245, 245, 245));
        campoPesquisa.setForeground(new Color(150, 150, 150));
        campoPesquisa.setText("Busque por marca ou modelo");
        
        // Placeholder logica
        campoPesquisa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campoPesquisa.getText().equals("Busque por marca ou modelo")) {
                    campoPesquisa.setText("");
                    campoPesquisa.setForeground(new Color(60, 60, 60));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campoPesquisa.getText().isEmpty()) {
                    campoPesquisa.setText("Busque por marca ou modelo");
                    campoPesquisa.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        // Listener para filtrar enquanto digita
        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarVeiculos(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarVeiculos(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarVeiculos(); }
        });

        // --- DIREITA: Lupa + Botão Mostrar Todos ---
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(245, 245, 245));

        // Ícone de Lupa
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        
        // NOVO BOTÃO: Mostrar todos
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        btnMostrarTodos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMostrarTodos.setForeground(Color.WHITE);
        btnMostrarTodos.setBackground(new Color(100, 100, 100)); // Cinza escuro
        btnMostrarTodos.setBorderPainted(false);
        btnMostrarTodos.setFocusPainted(false);
        btnMostrarTodos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Ação: Limpa o campo e reseta o filtro
        btnMostrarTodos.addActionListener(e -> {
            campoPesquisa.setText(""); // Isso dispara o filtrarVeiculos() automaticamente
            campoPesquisa.setText("Busque por marca ou modelo"); // Restaura placeholder visualmente
            campoPesquisa.setForeground(new Color(150, 150, 150));
            filtrarVeiculos(); // Garante que mostre tudo
            // Opcional: tirar o foco do campo para mostrar o placeholder corretamente
            btnMostrarTodos.requestFocusInWindow(); 
        });

        rightPanel.add(searchIcon);
        rightPanel.add(btnMostrarTodos);

        // Montagem do container
        searchContainer.add(menuIcon, BorderLayout.WEST);
        searchContainer.add(campoPesquisa, BorderLayout.CENTER);
        searchContainer.add(rightPanel, BorderLayout.EAST);

        painelExterno.add(searchContainer);
        
        return painelExterno;
    }
    
    /**
     * Cria a seção "Mais Populares" com categorias clicáveis
     */
    private JPanel criarSecaoMaisPopulares() {
        JPanel secao = new JPanel();
        secao.setLayout(new BoxLayout(secao, BoxLayout.Y_AXIS));
        secao.setBackground(Color.WHITE);
        secao.setBorder(new EmptyBorder(40, 0, 40, 0));
    
        // Título
        JLabel titulo = new JLabel("Mais populares");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT); // Importante para BoxLayout
        secao.add(titulo);
        secao.add(Box.createVerticalStrut(30));
    
        // Grid de categorias
        // GridLayout(linhas, colunas, gapH, gapV)
        JPanel gridCategorias = new JPanel(new GridLayout(2, 3, 20, 20)); 
        gridCategorias.setBackground(Color.WHITE);
    
        String[] categorias = {"PICAPE", "GOL", "SUV", "HB20", "DODGE RAM", "NISSAN Z"};
        Color[] cores = {
            new Color(30, 30, 30), new Color(220, 220, 220), new Color(200, 30, 30),
            new Color(30, 100, 200), new Color(139, 69, 19), new Color(0, 100, 200)
        };
    
        for (int i = 0; i < categorias.length; i++) {
            JPanel card = criarCardCategoria(categorias[i], cores[i]);
            gridCategorias.add(card);
        }
    
        // TRUQUE: Painel "Wrapper" para centralizar o grid
        JPanel wrapperGrid = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperGrid.setBackground(Color.WHITE);
        wrapperGrid.add(gridCategorias);
        
        secao.add(wrapperGrid);
    
        return secao;
    }
    
    /**
     * Cria um card de categoria clicável
     */
    private JPanel criarCardCategoria(String nome, Color corFundo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(corFundo);
        card.setPreferredSize(new Dimension(200, 150));
        card.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        JLabel label = new JLabel(nome, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(corFundo.getRed() < 128 ? Color.WHITE : Color.BLACK);
        label.setOpaque(false);
        
        card.add(label, BorderLayout.CENTER);
        
        // Efeito hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new LineBorder(new Color(139, 69, 19), 3));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new LineBorder(new Color(200, 200, 200), 1));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Filtrar por categoria
                campoPesquisa.setText(nome);
                campoPesquisa.setForeground(new Color(60, 60, 60));
                filtrarVeiculos();
            }
        });
        
        return card;
    }
    
    /**
     * Cria a seção de ofertas com grid de cards
     */
    private JPanel criarSecaoOfertas() {
        JPanel secao = new JPanel();
        secao.setLayout(new BoxLayout(secao, BoxLayout.Y_AXIS));
        secao.setBackground(Color.WHITE);
        secao.setBorder(new EmptyBorder(40, 0, 40, 0)); // Removi padding lateral fixo

        // Título Centralizado
        JLabel titulo = new JLabel("OFERTAS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT); // <--- MUDANÇA AQUI
        secao.add(titulo);
        secao.add(Box.createVerticalStrut(30));

        // Painel de grid de ofertas
        painelOfertas = new JPanel();
        // GridLayout com 3 colunas fixas e espaçamento de 20px
        painelOfertas.setLayout(new GridLayout(0, 3, 20, 20)); 
        painelOfertas.setBackground(Color.WHITE);
        
        // Wrapper para centralizar o grid de ofertas
        JPanel wrapperOfertas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperOfertas.setBackground(Color.WHITE);
        wrapperOfertas.add(painelOfertas);

        secao.add(wrapperOfertas);

        return secao;
    }
    
    /**
     * Atualiza o grid de ofertas com os veículos
     */
    private void atualizarGridOfertas() {
        painelOfertas.removeAll();
        cardsVeiculos.clear();
        
        for (Veiculo veiculo : veiculos) {
            CardVeiculo card = new CardVeiculo(veiculo);
            cardsVeiculos.add(card);
            painelOfertas.add(card);
        }
        
        painelOfertas.revalidate();
        painelOfertas.repaint();
    }
    
    /**
     * Filtra os veículos baseado no texto de pesquisa
     */
    private void filtrarVeiculos() {
        String texto = campoPesquisa.getText().toLowerCase();
        
        // Se o campo está vazio ou com placeholder, mostra todos
        if (texto.isEmpty() || texto.equals("busque por marca ou modelo")) {
            for (CardVeiculo card : cardsVeiculos) {
                card.setVisible(true);
            }
        } else {
            // Filtra por marca ou modelo
            for (CardVeiculo card : cardsVeiculos) {
                Veiculo v = card.getVeiculo();
                String marca = v.getMarca() != null ? v.getMarca().toLowerCase() : "";
                String modelo = v.getModelo() != null ? v.getModelo().toLowerCase() : "";
                
                boolean match = marca.contains(texto) || modelo.contains(texto);
                card.setVisible(match);
            }
        }
        
        painelOfertas.revalidate();
        painelOfertas.repaint();
    }
    
    /**
     * Cria o footer com links e informações
     */
    private JPanel criarFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(new Color(101, 67, 33)); // Marrom escuro
        footer.setBorder(new EmptyBorder(40, 50, 30, 50));
        
        // Grid de links em 3 colunas
        JPanel linksGrid = new JPanel(new GridLayout(1, 3, 30, 0));
        linksGrid.setBackground(new Color(101, 67, 33));
        linksGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Coluna 1: Veículos
        JPanel col1 = criarColunaFooter("Veículos", new String[]{
            "Novos", "Usados", "Elétricos & Híbridos", "SUVs", "Picapes", "Esportivos"
        });
        
        // Coluna 2: Serviços
        JPanel col2 = criarColunaFooter("Serviços", new String[]{
            "Simulação de Financiamento", "Agendar Test-Drive", "Manutenção & Peças",
            "Consórcio Blade", "Vender Carro Usado", "Seguro Auto"
        });
        
        // Coluna 3: A Blade Motors
        JPanel col3 = criarColunaFooter("A Blade Motors", new String[]{
            "Sobre Nós", "Encontrar Concessionária", "Trabalhe Conosco",
            "Imprensa", "Contato", "Blog"
        });
        
        linksGrid.add(col1);
        linksGrid.add(col2);
        linksGrid.add(col3);
        
        footer.add(linksGrid);
        footer.add(Box.createVerticalStrut(30));
        
        // Informações da empresa
        JLabel infoEmpresa = new JLabel(
            "<html><div style='text-align: center; color: white;'>" +
            "Blade Motors S.A: Avenida da Performance, 1500, Edifício Apex Tower, 22º andar, " +
            "Vila Olímpia - São Paulo SP CEP: 04551-010 - CNPJ: 12.345.678/0001-99" +
            "</div></html>"
        );
        infoEmpresa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoEmpresa.setForeground(Color.WHITE);
        infoEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.add(infoEmpresa);
        
        footer.add(Box.createVerticalStrut(15));
        
        // Copyright e links legais
        JLabel copyright = new JLabel(
            "<html><div style='text-align: center; color: white;'>" +
            "©2025 Blade Motors S.A. Todos os direitos reservados. " +
            "[Política de Privacidade] | [Termos de Uso] | [Mapa do Site]" +
            "</div></html>"
        );
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyright.setForeground(Color.WHITE);
        copyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.add(copyright);
        
        return footer;
    }
    
    /**
     * Cria uma coluna do footer
     */
    private JPanel criarColunaFooter(String titulo, String[] links) {
        JPanel coluna = new JPanel();
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setBackground(new Color(101, 67, 33));
        
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tituloLabel.setForeground(Color.WHITE);
        coluna.add(tituloLabel);
        coluna.add(Box.createVerticalStrut(10));
        
        for (String link : links) {
            JLabel linkLabel = new JLabel(link);
            linkLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            linkLabel.setForeground(new Color(220, 220, 220));
            linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Efeito hover
            linkLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    linkLabel.setForeground(Color.WHITE);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    linkLabel.setForeground(new Color(220, 220, 220));
                }
            });
            
            coluna.add(linkLabel);
            coluna.add(Box.createVerticalStrut(5));
        }
        
        return coluna;
    }
    
    /**
     * Busca o veículo do cliente (simulado)
     * Regra: Cada cliente pode possuir apenas 1 veículo
     * 
     * @param cliente Cliente para buscar o veículo
     * @return Veiculo do cliente ou null se não possuir
     */
    private Veiculo buscarVeiculoDoCliente(Cliente cliente) {
        // Mock: Se o cliente for "João", retorna um veículo específico
        if (cliente != null && cliente.getUsuario() != null) {
            String nomeCliente = cliente.getUsuario().getNome();
            
            if (nomeCliente != null && nomeCliente.equalsIgnoreCase("João")) {
                // Retorna um veículo específico para o cliente de teste
                Veiculo veiculo = new Veiculo("Hatch Moderno", "Hyundai", 2022, "ABC-1234", "CHASSI1");
                veiculo.setIdVeiculo(1);
                veiculo.setPreco(75000.00);
                return veiculo;
            }
        }
        
        // Para outros clientes, retorna null (não possui veículo)
        return null;
    }
    
    /**
     * Popula a lista com veículos fictícios
     */
    private void populaVeiculosFake() {
        veiculos.clear();
        
        // Picape Ágil - WorkPro 1.4
        Veiculo v1 = new Veiculo("WorkPro 1.4", "Picape Ágil", 2023, "ABC-1234", "CHASSI001");
        v1.setPreco(85000.00);
        v1.setIdVeiculo(1);
        veiculos.add(v1);
        
        // Hatch Compacto - Gol G-VIII
        Veiculo v2 = new Veiculo("Gol G-VIII", "Hatch Compacto", 2024, "DEF-5678", "CHASSI002");
        v2.setPreco(65000.00);
        v2.setIdVeiculo(2);
        veiculos.add(v2);
        
        // SUV Premium - Conqueror X7
        Veiculo v3 = new Veiculo("Conqueror X7", "SUV Premium", 2024, "GHI-9012", "CHASSI003");
        v3.setPreco(120000.00);
        v3.setIdVeiculo(3);
        veiculos.add(v3);
        
        // Hatch Moderno - HB-S 1.0 Turbo
        Veiculo v4 = new Veiculo("HB-S 1.0 Turbo", "Hatch Moderno", 2024, "JKL-3456", "CHASSI004");
        v4.setPreco(75000.00);
        v4.setIdVeiculo(4);
        veiculos.add(v4);
        
        // Picape de Luxo - Dodge Ram 2500
        Veiculo v5 = new Veiculo("Dodge Ram 2500", "Picape de Luxo", 2023, "MNO-7890", "CHASSI005");
        v5.setPreco(280000.00);
        v5.setIdVeiculo(5);
        veiculos.add(v5);
        
        // Esportivo Lendário - Nissan Z
        Veiculo v6 = new Veiculo("Nissan Z", "Esportivo Lendário", 2024, "PQR-1357", "CHASSI006");
        v6.setPreco(350000.00);
        v6.setIdVeiculo(6);
        veiculos.add(v6);
        
        // Adicionar mais veículos para preencher o grid
        Veiculo v7 = new Veiculo("Civic", "Sedan Executivo", 2024, "STU-2468", "CHASSI007");
        v7.setPreco(145000.00);
        v7.setIdVeiculo(7);
        veiculos.add(v7);
        
        Veiculo v8 = new Veiculo("Corolla", "Sedan Premium", 2024, "VWX-3691", "CHASSI008");
        v8.setPreco(135000.00);
        v8.setIdVeiculo(8);
        veiculos.add(v8);
        
        Veiculo v9 = new Veiculo("Onix", "Hatch Compacto", 2024, "YZA-4826", "CHASSI009");
        v9.setPreco(70000.00);
        v9.setIdVeiculo(9);
        veiculos.add(v9);
    }
    
    /**
     * Classe interna para o card de veículo
     */
    private class CardVeiculo extends JPanel {
        private Veiculo veiculo;
        private JLabel imagemLabel;
        private JLabel tituloLabel;
        private JLabel subtituloLabel;
        private JLabel descricaoLabel;
        private JLabel precoLabel;
        private JButton btnDetalhes;
        private JButton btnComprar;
        
        public CardVeiculo(Veiculo veiculo) {
            this.veiculo = veiculo;
            
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(250, 250, 250));
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
            ));
            setPreferredSize(new Dimension(350, 500));
            setMaximumSize(new Dimension(350, 500));
            
            // Imagem do carro (simulada com ícone)
            imagemLabel = new JLabel("🚗", SwingConstants.CENTER);
            imagemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 80));
            imagemLabel.setPreferredSize(new Dimension(0, 180));
            imagemLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
            imagemLabel.setBackground(new Color(240, 240, 240));
            imagemLabel.setOpaque(true);
            add(imagemLabel);
            add(Box.createVerticalStrut(15));
            
            // Título (categoria)
            String categoria = obterCategoria(veiculo);
            tituloLabel = new JLabel(categoria);
            tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            tituloLabel.setForeground(Color.BLACK);
            tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(tituloLabel);
            add(Box.createVerticalStrut(5));
            
            // Subtítulo (modelo)
            subtituloLabel = new JLabel(veiculo.getModelo());
            subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtituloLabel.setForeground(new Color(100, 100, 100));
            subtituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(subtituloLabel);
            add(Box.createVerticalStrut(10));
            
            // Descrição
            String descricao = gerarDescricao(veiculo);
            descricaoLabel = new JLabel("<html><div style='width: 300px;'>" + descricao + "</div></html>");
            descricaoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descricaoLabel.setForeground(new Color(80, 80, 80));
            descricaoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(descricaoLabel);
            add(Box.createVerticalStrut(15));
            
            // Preço
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            precoLabel = new JLabel(format.format(veiculo.getPreco()));
            precoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            precoLabel.setForeground(new Color(139, 69, 19));
            precoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(precoLabel);
            add(Box.createVerticalStrut(20));
            
            // Botões
            JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            painelBotoes.setBackground(new Color(250, 250, 250));
            painelBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Botão Detalhes (outline)
            btnDetalhes = new JButton("DETALHES");
            btnDetalhes.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDetalhes.setForeground(new Color(139, 69, 19));
            btnDetalhes.setBackground(Color.WHITE);
            btnDetalhes.setBorder(new LineBorder(new Color(139, 69, 19), 2));
            btnDetalhes.setPreferredSize(new Dimension(120, 35));
            btnDetalhes.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDetalhes.addActionListener(e -> mostrarDetalhes());
            
            // Botão Comprar (sólido)
            btnComprar = new JButton("COMPRAR");
            btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnComprar.setForeground(Color.WHITE);
            btnComprar.setBackground(new Color(101, 67, 33));
            btnComprar.setBorder(null);
            btnComprar.setPreferredSize(new Dimension(120, 35));
            btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnComprar.addActionListener(e -> abrirSidebarCompra());
            
            painelBotoes.add(btnDetalhes);
            painelBotoes.add(btnComprar);
            add(painelBotoes);
        }
        
        public Veiculo getVeiculo() {
            return veiculo;
        }
        
        private String obterCategoria(Veiculo v) {
            String modelo = v.getModelo().toLowerCase();
            if (modelo.contains("workpro") || modelo.contains("ram") || modelo.contains("picape")) {
                return "Picape Ágil";
            } else if (modelo.contains("gol") || modelo.contains("onix") || modelo.contains("hb")) {
                return "Hatch Compacto";
            } else if (modelo.contains("conqueror") || modelo.contains("suv")) {
                return "SUV Premium";
            } else if (modelo.contains("nissan z") || modelo.contains("esportivo")) {
                return "Esportivo Lendário";
            } else if (modelo.contains("civic") || modelo.contains("corolla")) {
                return "Sedan Executivo";
            }
            return "Veículo Premium";
        }
        
        private String gerarDescricao(Veiculo v) {
            String modelo = v.getModelo().toLowerCase();
            if (modelo.contains("workpro")) {
                return "Picape robusta ideal para trabalho e lazer. Motor 1.4 turbo com excelente desempenho e economia.";
            } else if (modelo.contains("gol")) {
                return "Hatch compacto e versátil. Perfeito para o dia a dia urbano com conforto e tecnologia.";
            } else if (modelo.contains("conqueror")) {
                return "SUV premium com espaço interno generoso e tecnologia de ponta. Ideal para família e aventuras.";
            } else if (modelo.contains("hb")) {
                return "Hatch moderno com design arrojado e motor turbo. Economia e performance em um só carro.";
            } else if (modelo.contains("ram")) {
                return "Picape de luxo com capacidade de carga excepcional. Conforto e potência para qualquer desafio.";
            } else if (modelo.contains("nissan z")) {
                return "Esportivo lendário com motor V6 biturbo. Performance pura e design icônico.";
            } else if (modelo.contains("civic")) {
                return "Sedan executivo com tecnologia avançada e design sofisticado. Elegância e performance.";
            } else if (modelo.contains("corolla")) {
                return "Sedan premium com confiabilidade comprovada. Conforto e eficiência para toda a família.";
            } else if (modelo.contains("onix")) {
                return "Hatch compacto com excelente custo-benefício. Moderno, econômico e cheio de tecnologia.";
            }
            return "Veículo de alta qualidade com tecnologia de ponta e design moderno.";
        }
        
        private void mostrarDetalhes() {
            JOptionPane.showMessageDialog(
                CardVeiculo.this,
                "Detalhes do Veículo:\n\n" +
                "Modelo: " + veiculo.getModelo() + "\n" +
                "Marca: " + veiculo.getMarca() + "\n" +
                "Ano: " + veiculo.getAno() + "\n" +
                "Placa: " + veiculo.getPlaca() + "\n" +
                "Preço: " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(veiculo.getPreco()),
                "Detalhes do Veículo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        
        private void abrirSidebarCompra() {
            new SidebarCompra(parentFrame, veiculo).setVisible(true);
        }
    }
    
    /**
     * Classe para a sidebar de compra (modal lateral)
     */
    @SuppressWarnings("unused")
    public static class SidebarCompra extends JDialog {
        private final Veiculo veiculo;
        
        public SidebarCompra(JFrame parent, Veiculo veiculo) {
            super(parent, "Finalizar Compra", true);
            this.veiculo = veiculo;
            
            setSize(400, 600);
            setLocationRelativeTo(parent);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBorder(new EmptyBorder(30, 30, 30, 30));
            content.setBackground(Color.WHITE);
            
            // Título
            JLabel titulo = new JLabel("Finalizar Compra");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(titulo);
            content.add(Box.createVerticalStrut(20));
            
            // Informações do veículo
            JLabel modelo = new JLabel("Modelo: " + this.veiculo.getModelo());
            modelo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            modelo.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(modelo);
            
            JLabel marca = new JLabel("Marca: " + this.veiculo.getMarca());
            marca.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            marca.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(marca);
            
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            JLabel preco = new JLabel("Preço: " + format.format(this.veiculo.getPreco()));
            preco.setFont(new Font("Segoe UI", Font.BOLD, 18));
            preco.setForeground(new Color(139, 69, 19));
            preco.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(Box.createVerticalStrut(10));
            content.add(preco);
            content.add(Box.createVerticalStrut(30));
            
            // Campos do formulário
            content.add(new JLabel("Nome Completo:"));
            JTextField nomeField = new JTextField();
            nomeField.setPreferredSize(new Dimension(0, 35));
            content.add(nomeField);
            content.add(Box.createVerticalStrut(15));
            
            content.add(new JLabel("CPF:"));
            JTextField cpfField = new JTextField();
            cpfField.setPreferredSize(new Dimension(0, 35));
            content.add(cpfField);
            content.add(Box.createVerticalStrut(15));
            
            content.add(new JLabel("Telefone:"));
            JTextField telefoneField = new JTextField();
            telefoneField.setPreferredSize(new Dimension(0, 35));
            content.add(telefoneField);
            content.add(Box.createVerticalStrut(30));
            
            // Botão finalizar
            JButton btnFinalizar = new JButton("FINALIZAR COMPRA");
            btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnFinalizar.setForeground(Color.GREEN);
            btnFinalizar.setBackground(new Color(101, 67, 33));
            btnFinalizar.setPreferredSize(new Dimension(0, 45));
            btnFinalizar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnFinalizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnFinalizar.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Compra realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
            content.add(btnFinalizar);
            
            add(content);
        }
    }
}

