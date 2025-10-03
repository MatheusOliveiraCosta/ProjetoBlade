/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projetoblade;

/**
 *
 * @author mathe
 */
public class LaudoTecnico {
    public double calcularCustoTotal() {
        // Lógica complexa para buscar o preço de cada peça no estoque...
        // ...e calcular o custo da mão de obra.
        // **Isto é uma responsabilidade de cálculo, não de representação de dados.**
        double custo = 0.0; 
        // ...
        return custo;
    }
    
    public String gerarTextoParaImpressao() {
        // Monta um cabeçalho, rodapé, formata os dados em um texto longo...
        // **Isto é uma responsabilidade de formatação, não de representação de dados.**
        StringBuilder sb = new StringBuilder();
        sb.append("--- LAUDO TÉCNICO ---");
        // ...
        return sb.toString();
    }
    
    public void salvarEmPDF(String caminhoDoArquivo) {
        // Abre um arquivo, usa uma biblioteca de PDF para escrever os dados...
        // **Isto é uma responsabilidade de geração de arquivos, não de representação de dados.**
        try {
            PdfWriter writer = new PdfWriter(new File(caminhoDoArquivo));
            // ... lógica complexa com a biblioteca iTextPDF ...
        } catch (Exception e) {
            //...
        }
    }
}
