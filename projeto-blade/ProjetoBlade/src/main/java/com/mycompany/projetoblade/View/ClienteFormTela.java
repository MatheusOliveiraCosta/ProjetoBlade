public class ClienteFormTela extends JDialog {

    private JTextField nomeField = new JTextField();
    private JTextField emailField = new JTextField();
    private JTextField cpfField = new JTextField();
    private JTextField enderecoField = new JTextField();

    public ClienteFormTela(Dialog owner) {
        super(owner, "Formulário de Cliente", true);
        setSize(450, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Painel do Formulário (Centro) ---
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        painelForm.add(new JLabel("Nome Completo:"));
        painelForm.add(nomeField);
        painelForm.add(new JLabel("Email:"));
        painelForm.add(emailField);
        painelForm.add(new JLabel("CPF:"));
        painelForm.add(cpfField);
        painelForm.add(new JLabel("Endereço:"));
        painelForm.add(enderecoField);
        
        add(painelForm, BorderLayout.CENTER);
        
        // --- Painel de Botões (Sul) ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painelBotoes, BorderLayout.SOUTH);
        
        // --- Ações dos Botões ---
        btnSalvar.addActionListener(e -> {
            // Simulação: Apenas exibe os dados e fecha a tela
            System.out.println("Salvando cliente: " + nomeField.getText());
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso (simulação)!");
            dispose();
        });
        
        btnCancelar.addActionListener(e -> {
            dispose();
        });
    }
}