package com.mycompany.projetoblade.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.mycompany.projetoblade.model.Manutencao;
import com.mycompany.projetoblade.model.Veiculo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Tela de meu veículo - Modal com design moderno
 * Agora suporta apenas 1 veículo por cliente
 */
public class MeusVeiculosTela extends JDialog {
    
    private Veiculo veiculo;
    private JFrame parentFrame;
    private com.mycompany.projetoblade.service.ManutencaoService manutencaoService;
    private com.mycompany.projetoblade.service.VeiculoService veiculoService;
    
    public MeusVeiculosTela(JFrame parent, Veiculo veiculo, com.mycompany.projetoblade.service.ManutencaoService service, com.mycompany.projetoblade.service.VeiculoService veiculoService) {
        super(parent, true); // Modal
        this.manutencaoService = service;
        this.parentFrame = parent;
        setUndecorated(true); // Remove barra de título padrão
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.veiculo = veiculo;
        this.veiculoService = veiculoService;
        this.manutencaoService = manutencaoService;
        
        // Fundo cinza claro
        getContentPane().setBackground(new Color(0xD9D9D9));
        
        // Aplicar bordas arredondadas na janela
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 900, 600, 15, 15));
        
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(0xD9D9D9));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // === TOPO ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        topPanel.setBackground(new Color(0xD9D9D9));
        
        // Ícone da marca à esquerda
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        try {
            java.net.URL logoUrl = getClass().getResource("/images/logo.png");
            if (logoUrl == null) {
                logoUrl = getClass().getClassLoader().getResource("images/logo.png");
            }
            
            if (logoUrl != null) {
                BufferedImage imgLogo = ImageIO.read(logoUrl);
                int targetHeight = 40;
                double ratio = (double) imgLogo.getWidth() / imgLogo.getHeight();
                int targetWidth = (int) (targetHeight * ratio);
                Image scaledLogo = imgLogo.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledLogo));
            } else {
                logoLabel.setText("BLADE MOTORS");
                logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                logoLabel.setForeground(Color.BLACK);
            }
        } catch (Exception e) {
            logoLabel.setText("BLADE MOTORS");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            logoLabel.setForeground(Color.BLACK);
        }
        
        // Título "Olá, Cliente" centralizado
        JLabel titulo = new JLabel("Olá, Cliente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.BLACK);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Botão X (fechar) à direita
        JButton btnFechar = new JButton("✕");
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnFechar.setForeground(Color.BLACK);
        btnFechar.setBorderPainted(false);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setFocusPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dispose());
        
        topPanel.add(logoLabel, BorderLayout.WEST);
        topPanel.add(titulo, BorderLayout.CENTER);
        topPanel.add(btnFechar, BorderLayout.EAST);
        
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // === TÍTULO "Meu Veículo" (inclui marca/modelo) ===
        String marcaModelo = this.veiculo != null ? (this.veiculo.getMarca() + " - " + this.veiculo.getModelo()) : "Meu Veículo";
        JLabel tituloVeiculo = new JLabel(marcaModelo);
        tituloVeiculo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tituloVeiculo.setForeground(Color.BLACK);
        tituloVeiculo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tituloVeiculo);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // === CARD CENTRALIZADO ===
        JPanel painelCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        painelCard.setOpaque(false);
        painelCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Criar card para o único veículo
        CardVeiculoCliente card = new CardVeiculoCliente(veiculo);
        painelCard.add(card);
        
        mainPanel.add(painelCard);
        
        // Adicionar painel principal ao dialog
        add(mainPanel);
    }
    
    /**
     * Classe interna para o card de veículo do cliente
     */
    public class CardVeiculoCliente extends JPanel {
        private Veiculo veiculo;
        private JPanel painelStatus;
        
        public CardVeiculoCliente(Veiculo veiculo) {
            this.veiculo = veiculo;
            
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(0x666666)); // Cinza escuro
            setBorder(new EmptyBorder(15, 15, 15, 15));
            setPreferredSize(new Dimension(380, 450));
            setMaximumSize(new Dimension(380, 450));
            
            // Aplicar bordas arredondadas
            putClientProperty(FlatClientProperties.STYLE, 
                "arc: 30;" +
                "borderWidth: 0;");
            
            // === IMAGEM DO CARRO ===
            JPanel painelImagem = new JPanel(new BorderLayout());
            painelImagem.setBackground(Color.WHITE);
            painelImagem.setPreferredSize(new Dimension(0, 250));
            painelImagem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
            painelImagem.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            // Aplicar bordas arredondadas no painel da imagem
            painelImagem.putClientProperty(FlatClientProperties.STYLE, 
                "arc: 15;" +
                "borderWidth: 0;");
            
            // Ícone de carro (simulado - você pode carregar imagem real)
            JLabel imagemCarro = new JLabel("🚗", SwingConstants.CENTER);
            imagemCarro.setFont(new Font("Segoe UI", Font.PLAIN, 120));
            
            // Tentar carregar imagem do veículo se disponível
            try {
                // Você pode adicionar lógica para carregar imagem baseada no modelo/ID do veículo
                // Por enquanto, usa o ícone
            } catch (Exception e) {
                // Mantém o ícone padrão
            }
            
            painelImagem.add(imagemCarro, BorderLayout.CENTER);
            add(painelImagem);
            add(Box.createVerticalStrut(15));
            
            // === INFORMAÇÕES DO VEÍCULO ===
            // Modelo
            String categoria = obterCategoria(veiculo);
            JLabel labelModelo = new JLabel(categoria);
            labelModelo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            labelModelo.setForeground(Color.BLACK);
            labelModelo.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(labelModelo);
            add(Box.createVerticalStrut(5));
            
            // Placa
            String placa = veiculo.getPlaca() != null && !veiculo.getPlaca().isEmpty() 
                ? veiculo.getPlaca() 
                : "Sem placa";
            JLabel labelPlaca = new JLabel("Placa: " + placa);
            labelPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            labelPlaca.setForeground(new Color(60, 60, 60));
            labelPlaca.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(labelPlaca);
            add(Box.createVerticalStrut(20));
            
            // === PAINEL DE STATUS DINÂMICO (escondido se não houver manutenção ativa) ===
            painelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
            painelStatus.setOpaque(true);
            painelStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            painelStatus.setVisible(false);
            add(painelStatus);
            add(Box.createVerticalStrut(10));
            
            // === BOTÕES ===
            JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            painelBotoes.setOpaque(false);
            painelBotoes.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Botão "Agendar serviço"
            JButton btnAgendar = new JButton("Agendar serviço");
            btnAgendar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnAgendar.setForeground(Color.BLACK);
            btnAgendar.setBackground(new Color(0x999999)); // Cinza médio
            btnAgendar.setBorderPainted(false);
            btnAgendar.setFocusPainted(false);
            btnAgendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAgendar.setPreferredSize(new Dimension(150, 35));
            
            btnAgendar.putClientProperty(FlatClientProperties.STYLE, 
                "arc: 10;" +
                "borderWidth: 0;");
            
            btnAgendar.addActionListener(e -> {
                // Abre a tela de solicitação de manutenção já preenchida com a placa
                SolicitarManutencaoTela.mostrar(MeusVeiculosTela.this.parentFrame, placa);
            });
            
            // Botão "Detalhes" — mostra marca e modelo
            JButton btnHistorico = new JButton("Detalhes");
            btnHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnHistorico.setForeground(Color.BLACK);
            btnHistorico.setBackground(new Color(0x999999)); // Cinza médio
            btnHistorico.setBorderPainted(false);
            btnHistorico.setFocusPainted(false);
            btnHistorico.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnHistorico.setPreferredSize(new Dimension(150, 35));
            
            btnHistorico.putClientProperty(FlatClientProperties.STYLE, 
                "arc: 10;" +
                "borderWidth: 0;");
            
            btnHistorico.addActionListener(e -> {
                mostrarDetalhes();
            });
            
            List<Manutencao> historico = manutencaoService.buscarPorVeiculo(veiculo.getIdVeiculo());

            boolean temManutencaoAtiva = false;

            // 2. Verifica se a última manutenção está ativa
            if (!historico.isEmpty()) {
                // Pega a última da lista (assumindo que a lista insere no final)
                Manutencao ultima = historico.get(historico.size() - 1);
                String status = ultima.getStatus().toUpperCase();
                
                if (status.equals("AGUARDANDO") || status.equals("EM_ANDAMENTO") || status.equals("EM DIAGNÓSTICO")) {
                    temManutencaoAtiva = true;
                }
            }

            // 3. Define a visibilidade do botão
            if (temManutencaoAtiva) {
                // Se já tem uma rolando, esconde o botão de solicitar nova
                btnAgendar.setVisible(false); 
                
                // Opcional: Adicionar um texto avisando
                JLabel aviso = new JLabel("<html><center><font color='red'>Manutenção em andamento</font></center></html>");
                painelBotoes.add(aviso);
            } else {
                // Se não tem (ou a última já acabou), mostra o botão
                btnAgendar.setVisible(true);
            }

            // Adiciona ao painel
            if (btnAgendar.isVisible()) {
                painelBotoes.add(btnAgendar);
            }
            painelBotoes.add(btnHistorico);

            painelBotoes.add(btnAgendar);
            
            // Botão "Solicitar Nova Manutenção" (mais explícito)
            JButton btnSolicitarNova = new JButton("Solicitar Nova Manutenção");
            btnSolicitarNova.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnSolicitarNova.setForeground(Color.WHITE);
            btnSolicitarNova.setBackground(new Color(0x0066CC));
            btnSolicitarNova.setBorderPainted(false);
            btnSolicitarNova.setFocusPainted(false);
            btnSolicitarNova.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSolicitarNova.setPreferredSize(new Dimension(200, 35));
            btnSolicitarNova.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderWidth: 0;");
            btnSolicitarNova.addActionListener(e -> {
                // Open SolicitarManutencaoTela with shared services and prefilled placa
                SolicitarManutencaoTela tela = new SolicitarManutencaoTela(MeusVeiculosTela.this.parentFrame, MeusVeiculosTela.this.veiculoService, MeusVeiculosTela.this.manutencaoService);
                if (tela != null) tela.setPlaca(placa);
                tela.setVisible(true);
            });
            painelBotoes.add(btnSolicitarNova);
            painelBotoes.add(btnHistorico);
            add(painelBotoes);

            // Após construir os botões, carrega e atualiza o painel de status
            atualizarPainelDeStatus(placa);
        }
        
        /**
         * Obtém a categoria do veículo baseado no modelo
         */
        private String obterCategoria(Veiculo v) {
            String modelo = v.getModelo() != null ? v.getModelo().toLowerCase() : "";
            if (modelo.contains("i20") || modelo.contains("hb") || modelo.contains("hatch")) {
                return "Hatch Moderno";
            } else if (modelo.contains("saveiro") || modelo.contains("ram") || modelo.contains("picape") || modelo.contains("pickup")) {
                return "Picape Ágil";
            } else if (modelo.contains("suv") || modelo.contains("conqueror")) {
                return "SUV Premium";
            } else if (modelo.contains("gol") || modelo.contains("onix")) {
                return "Hatch Compacto";
            } else if (modelo.contains("civic") || modelo.contains("corolla")) {
                return "Sedan Executivo";
            }
            return "Veículo";
        }
        
        /**
         * Mostra o histórico de manutenções do veículo
         */
        private void mostrarDetalhes() {
            String placa = veiculo.getPlaca() != null && !veiculo.getPlaca().isEmpty() 
                ? veiculo.getPlaca() 
                : "N/A";
            
            String marca = veiculo.getMarca() != null ? veiculo.getMarca() : "N/A";
            String modelo = veiculo.getModelo() != null ? veiculo.getModelo() : "N/A";

            String detalhes = "Detalhes do Veículo:\n\n" +
                "Marca: " + marca + "\n" +
                "Modelo: " + modelo + "\n" +
                "Placa: " + placa;

            JOptionPane.showMessageDialog(this, detalhes, "Detalhes do Veículo", JOptionPane.INFORMATION_MESSAGE);
        }

        // Busca manutenções pela placa e exibe o painel de status apropriado
        private void atualizarPainelDeStatus(String placa) {
            try {
                if (MeusVeiculosTela.this.manutencaoService == null) {
                    painelStatus.setVisible(false);
                    return;
                }

                java.util.List<com.mycompany.projetoblade.model.Manutencao> list = MeusVeiculosTela.this.manutencaoService.buscarPorPlaca(placa);

                if (list == null || list.isEmpty()) {
                    painelStatus.setVisible(false);
                    return;
                }

                // Encontra a manutenção mais recente (por data)
                com.mycompany.projetoblade.model.Manutencao chosen = list.stream()
                        .sorted((a,b) -> {
                            java.time.LocalDate da = a.getDataAgendamento() != null ? a.getDataAgendamento() : java.time.LocalDate.MIN;
                            java.time.LocalDate db = b.getDataAgendamento() != null ? b.getDataAgendamento() : java.time.LocalDate.MIN;
                            return db.compareTo(da);
                        }).findFirst().orElse(list.get(0));

                String status = chosen.getStatus() != null ? chosen.getStatus().toUpperCase() : "";
                painelStatus.removeAll();

                JLabel lblStatus = new JLabel();
                lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblStatus.setForeground(Color.WHITE);
                lblStatus.setOpaque(false);

                if ("AGUARDANDO".equals(status)) {
                    painelStatus.setBackground(new Color(0xFFCC00)); // Amarelo
                    lblStatus.setText("Aguardando");
                    painelStatus.add(lblStatus);
                    painelStatus.setVisible(true);
                } else if ("EM_ANDAMENTO".equals(status)) {
                    painelStatus.setBackground(new Color(0x007ACC)); // Azul
                    lblStatus.setText("Em Andamento");
                    painelStatus.add(lblStatus);
                    painelStatus.setVisible(true);
                } else if ("CONCLUIDO".equals(status) || "CONCLUIDO".equals(status.replaceAll("\u00E7","c"))) {
                    painelStatus.setBackground(new Color(0x00A859)); // Verde
                    lblStatus.setText("Concluído");
                    painelStatus.add(lblStatus);

                    // Botão para baixar laudo
                    JButton btnBaixar = new JButton("Baixar Laudo (PDF)");
                    btnBaixar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    btnBaixar.setForeground(Color.WHITE);
                    btnBaixar.setBackground(new Color(0x0066CC));
                    btnBaixar.setBorderPainted(false);
                    btnBaixar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnBaixar.addActionListener(evt -> {
                        JOptionPane.showMessageDialog(MeusVeiculosTela.this, "Iniciando download do laudo (simulado)...", "Baixar Laudo", JOptionPane.INFORMATION_MESSAGE);
                    });
                    painelStatus.add(Box.createHorizontalStrut(10));
                    painelStatus.add(btnBaixar);
                    painelStatus.setVisible(true);
                } else {
                    painelStatus.setVisible(false);
                }

                revalidate();
                repaint();

            } catch (Exception ex) {
                painelStatus.setVisible(false);
            }
        }
    }
    
    /**
     * Método para exibir a tela de meu veículo
     */
    public static void mostrar(JFrame parent, Veiculo veiculo) {
        SwingUtilities.invokeLater(() -> {
            MeusVeiculosTela tela = new MeusVeiculosTela(parent, veiculo, null, null);
            tela.setVisible(true);
        });
    }

    public static void mostrar(JFrame parent, Veiculo veiculo, com.mycompany.projetoblade.service.VeiculoService veiculoService, com.mycompany.projetoblade.service.ManutencaoService manutencaoService) {
        SwingUtilities.invokeLater(() -> {
            MeusVeiculosTela tela = new MeusVeiculosTela(parent, veiculo, manutencaoService, veiculoService);
            tela.setVisible(true);
        });
    }
}

