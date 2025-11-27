package com.mycompany.projetoblade.view;

import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;
import com.mycompany.projetoblade.service.ClienteService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de gerenciamento de clientes agora integrada ao ClienteService.
 */
public class TelaGerenciarClientes extends JFrame {

    private ClienteService clienteService;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    public TelaGerenciarClientes(JFrame parent, ClienteService clienteService) {
        this.clienteService = clienteService;

        setTitle("Gerenciamento de Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Top panel with title
        JLabel lblTitulo = new JLabel("Clientes Cadastrados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        // Table with columns: (hidden ID), NOME, CPF, CELULAR, EMAIL, ENDEREÇO
        String[] colunas = {"ID", "NOME", "CPF", "CELULAR", "EMAIL", "ENDEREÇO"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaClientes = new JTable(modeloTabela);
        // Hide ID column
        tabelaClientes.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaClientes.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaClientes.getColumnModel().getColumn(0).setWidth(0);
        add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);

        // Bottom panel with Edit/Delete actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        bottomPanel.add(btnEditar);
        bottomPanel.add(btnExcluir);
        add(bottomPanel, BorderLayout.SOUTH);

        // Edit action: fetch cliente by ID and allow updating via simple dialogs
        btnEditar.addActionListener(e -> {
            int row = tabelaClientes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para editar.");
                return;
            }
            Integer id = (Integer) modeloTabela.getValueAt(row, 0);
            try {
                var opt = clienteService.buscarPorId(id);
                if (opt.isPresent()) {
                    Cliente c = opt.get();
                    String novoNome = JOptionPane.showInputDialog(this, "Nome:", c.getUsuario() != null ? c.getUsuario().getNome() : "");
                    String novoCpf = JOptionPane.showInputDialog(this, "CPF:", c.getCpf());
                    String novoCel = JOptionPane.showInputDialog(this, "Celular:", c.getCelular());
                    String novoEmail = JOptionPane.showInputDialog(this, "Email:", c.getUsuario() != null ? c.getUsuario().getEmail() : "");
                    String novoEndereco = JOptionPane.showInputDialog(this, "Endereço:", c.getEndereco());

                    if (novoNome != null && novoEmail != null) {
                        if (c.getUsuario() == null) c.setUsuario(new Usuario(novoNome, novoEmail, ""));
                        else { c.getUsuario().setNome(novoNome); c.getUsuario().setEmail(novoEmail); }
                        c.setCpf(novoCpf);
                        c.setCelular(novoCel);
                        c.setEndereco(novoEndereco);
                        clienteService.salvarCliente(c);
                        atualizarTabela(clienteService.listarTodos());
                        JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete action: call removerCliente with the selected ID
        btnExcluir.addActionListener(e -> {
            int row = tabelaClientes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para excluir.");
                return;
            }
            Integer id = (Integer) modeloTabela.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir o cliente ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean removed = clienteService.removerCliente(id);
                    if (removed) {
                        atualizarTabela(clienteService.listarTodos());
                        JOptionPane.showMessageDialog(this, "Cliente removido com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cliente não encontrado ou não foi possível remover.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao remover cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Register listener to auto-refresh when new clients are saved
        if (this.clienteService != null) {
            this.clienteService.addClienteListener(c -> SwingUtilities.invokeLater(() -> atualizarTabela(this.clienteService.listarTodos())));
        }

        // Initial load
        atualizarTabela(this.clienteService != null ? this.clienteService.listarTodos() : java.util.Collections.emptyList());
    }

    private void atualizarTabela(List<Cliente> clientes) {
        modeloTabela.setRowCount(0);
        for (Cliente c : clientes) {
            String nome = c.getUsuario() != null ? c.getUsuario().getNome() : "";
            String email = c.getUsuario() != null ? c.getUsuario().getEmail() : "";
            modeloTabela.addRow(new Object[]{nome, c.getCpf(), c.getCelular(), email, c.getEndereco()});
        }
    }

}