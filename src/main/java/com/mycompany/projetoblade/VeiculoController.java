/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projetoblade;

/**
 *
 * @author mathe
 */
public class VeiculoController {
    private VeiculoService veiculoService;
    private VeiculoFormTela view;

    public VeiculoController(VeiculoService service, VeiculoFormTela view) {
        this.veiculoService = service;
        this.view = view;

        // Adiciona um "ouvinte" para o clique do botão salvar
        this.view.addSalvarListener(e -> salvarVeiculo());
    }

    private void salvarVeiculo() {
        // 1. Pega os dados da View
        String marca = view.getMarca();
        String modelo = view.getModelo();
        // ... pega os outros campos

        // 2. Cria um objeto do Modelo
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMarca(marca);
        novoVeiculo.setModelo(modelo);
        // ... seta os outros atributos

        // 3. Chama o Serviço para a lógica de negócio
        try {
            veiculoService.salvar(novoVeiculo);
            // 4. Dá um feedback para o usuário através da View
            JOptionPane.showMessageDialog(view, "Veículo salvo com sucesso!");
            view.dispose(); // Fecha a janela de formulário
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void exibirTela() {
        view.setVisible(true);
    }
}
