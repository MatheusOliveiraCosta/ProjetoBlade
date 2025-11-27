import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelaGerenciarClientes extends JFrame {

    // ==================== MODELO DE DADOS ====================
    // Esta classe simples representa um cliente.
    // No futuro, isso mapearia para uma tabela no seu banco de dados.
    class Cliente {
        private int id;
        private String nome;

        public Cliente(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }
    // =========================================================


    // Componentes da Interface
    private JTextField txtId;
    private JTextField txtNome;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    // "Banco de Dados" em memória (ArrayList)
    private List<Cliente> clientesBd = new ArrayList<>();

    public TelaGerenciarClientes() {
        // Configurações básicas da janela
        setTitle("Gerenciamento de Clientes");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout(10, 10)); // Layout principal com espaçamento

        // Inicializa alguns dados de teste
        inicializarDadosTeste();

        // --- PAINEL SUPERIOR (Título e Formulário) ---
        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.Y_AXIS));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Clientes Cadastrados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelSuperior.add(lblTitulo);
        painelSuperior.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço

        // Painel de Formulário (Grid para alinhar labels e campos)
        JPanel painelFormulario = new JPanel(new GridLayout(2, 2, 5, 5));
        painelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        painelFormulario.add(txtId);

        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);
        
        // Limita a altura do painel de formulário
        painelFormulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        painelSuperior.add(painelFormulario);
        painelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- PAINEL DE BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnLimpar = new JButton("Limpar"); // Botão extra útil

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnPesquisar);
        painelBotoes.add(btnLimpar);

        painelSuperior.add(painelBotoes);

        // Adiciona o painel superior completo ao Norte do layout principal
        add(painelSuperior, BorderLayout.NORTH);


        // --- PAINEL INFERIOR (Tabela com Scroll) ---
        // Configuração da Tabela
        String[] colunas = {"ID", "Nome"};
        // Modelo da tabela que impede edição direta nas células
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };
        tabelaClientes = new JTable(modeloTabela);
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // O JScrollPane é o segredo para a barra de rolagem lateral
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        // Define que a barra vertical aparece se necessário
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Adiciona a tabela (dentro do scroll) ao Centro do layout principal
        add(scrollPane, BorderLayout.CENTER);

        // Listener para seleção na tabela (preenche os campos ao clicar na linha)
        tabelaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaClientes.getSelectedRow() != -1) {
                int linhaSelecionada = tabelaClientes.getSelectedRow();
                txtId.setText(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
                txtNome.setText(modeloTabela.getValueAt(linhaSelecionada, 1).toString());
                // Bloqueia edição do ID ao selecionar, pois ID geralmente não se muda
                txtId.setEditable(false);
            }
        });

        // --- AÇÕES DOS BOTÕES (CRUD) ---

        // CREATE (Salvar)
        btnSalvar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String nome = txtNome.getText();

                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O nome não pode ser vazio.");
                    return;
                }

                // Verifica se ID já existe (em DB real isso é automático)
                boolean idExiste = clientesBd.stream().anyMatch(c -> c.getId() == id);
                if (idExiste) {
                     JOptionPane.showMessageDialog(this, "Já existe um cliente com este ID. Use Editar.");
                     return;
                }

                Cliente novoCliente = new Cliente(id, nome);
                clientesBd.add(novoCliente);
                atualizarTabela(clientesBd); // Atualiza a UI com a nova lista
                limparCampos();
                JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID deve ser um número válido.");
            }
        });

        // READ (Pesquisar por nome)
        btnPesquisar.addActionListener(e -> {
            String termoPesquisa = txtNome.getText().toLowerCase();
            if (termoPesquisa.isEmpty()) {
                 atualizarTabela(clientesBd); // Se vazio, mostra tudo
                 return;
            }

            // Filtra a lista (simulando um "SELECT * WHERE nome LIKE...")
            List<Cliente> resultados = clientesBd.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(termoPesquisa))
                    .collect(Collectors.toList());
            
            atualizarTabela(resultados);
        });

        // UPDATE (Editar)
        btnEditar.addActionListener(e -> {
            int linhaSelecionada = tabelaClientes.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para editar.");
                return;
            }

            // O ID original está na tabela (hidden ou na coluna 0)
            int idOriginal = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            String novoNome = txtNome.getText();

            // Encontra o objeto na lista e atualiza
            for (Cliente c : clientesBd) {
                if (c.getId() == idOriginal) {
                    c.setNome(novoNome);
                    break;
                }
            }
            atualizarTabela(clientesBd);
            limparCampos();
            JOptionPane.showMessageDialog(this, "Cliente atualizado!");
        });

        // DELETE (Excluir)
        btnExcluir.addActionListener(e -> {
            int linhaSelecionada = tabelaClientes.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para excluir.");
                return;
            }

            int idParaExcluir = (int) modeloTabela.getValueAt(linhaSelecionada, 0);

            int confirmacao = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja excluir o cliente ID " + idParaExcluir + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                // Remove da lista usando lambda (Java 8+)
                clientesBd.removeIf(c -> c.getId() == idParaExcluir);
                atualizarTabela(clientesBd);
                limparCampos();
            }
        });

        // Botão Limpar (Helper)
        btnLimpar.addActionListener(e -> {
            limparCampos();
            atualizarTabela(clientesBd); // Reseta filtros de pesquisa
        });

        // Carga inicial dos dados na tabela
        atualizarTabela(clientesBd);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Este método é crucial. Ele limpa a JTable e a redesenha
     * baseando-se na lista fornecida. É assim que a interface
     * se mantém sincronizada com os dados.
     */
    private void atualizarTabela(List<Cliente> listaParaExibir) {
        // 1. Limpa as linhas atuais da tabela visual
        modeloTabela.setRowCount(0);

        // 2. Percorre a lista e adiciona as linhas novamente
        for (Cliente c : listaParaExibir) {
            modeloTabela.addRow(new Object[]{c.getId(), c.getNome()});
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtId.setEditable(true); // Libera o ID para novos cadastros
        txtNome.setText("");
        tabelaClientes.clearSelection();
    }

    private void inicializarDadosTeste() {
        // Adiciona dados fake para a tabela não começar vazia e testar o scroll
        for (int i = 1; i <= 30; i++) {
            clientesBd.add(new Cliente(i, "Cliente Exemplo " + i));
        }
    }


    public static void main(String[] args) {
        // Executa a interface na thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            new TelaGerenciarClientes().setVisible(true);
        });
    }
}