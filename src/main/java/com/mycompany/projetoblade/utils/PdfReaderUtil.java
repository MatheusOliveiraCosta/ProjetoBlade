package com.mycompany.projetoblade.utils;

import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.model.VeiculoEletrico;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário para leitura de dados de veículos a partir de arquivos PDF.
 * Aplicando SRP - responsável apenas por extrair dados de veículos de PDFs.
 */
public class PdfReaderUtil {
    
    /**
     * Carrega veículos de um arquivo PDF
     * @param caminhoPdf caminho para o arquivo PDF
     * @return lista de veículos lidos do PDF
     * @throws IOException se houver erro na leitura do arquivo
     */
    public static List<Veiculo> carregarVeiculosDoPdf(String caminhoPdf) throws IOException {
        List<Veiculo> veiculos = new ArrayList<>();
        
        try (PDDocument documento = PDDocument.load(new File(caminhoPdf))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(documento);
            
            // Processa cada linha do texto
            String[] linhas = texto.split("\\r?\\n");
            for (String linha : linhas) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue; // Pula linhas vazias e comentários
                }
                
                try {
                    Veiculo veiculo = parsearLinhaVeiculo(linha);
                    if (veiculo != null) {
                        veiculos.add(veiculo);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha: " + linha + " - " + e.getMessage());
                }
            }
        }
        
        return veiculos;
    }
    
    /**
     * Parseia uma linha de texto para criar um objeto Veiculo
     * @param linha linha de texto contendo dados do veículo
     * @return objeto Veiculo ou null se não conseguir parsear
     */
    private static Veiculo parsearLinhaVeiculo(String linha) {
        // Formato esperado: modelo;marca;ano;placa;chassi;preco;status;[tipo]
        // Para veículos elétricos: modelo;marca;ano;placa;chassi;preco;status;ELETRICO;autonomia;tipoBateria;tempoCarregamento
        String[] dados = linha.split(";");
        
        if (dados.length < 7) {
            System.err.println("Linha com formato inválido (mínimo 7 campos): " + linha);
            return null;
        }
        
        try {
            String modelo = dados[0].trim();
            String marca = dados[1].trim();
            int ano = Integer.parseInt(dados[2].trim());
            String placa = dados[3].trim();
            String chassi = dados[4].trim();
            double preco = Double.parseDouble(dados[5].trim());
            String status = dados[6].trim();
            
            // Verifica se é um veículo elétrico
            if (dados.length >= 11 && "ELETRICO".equalsIgnoreCase(dados[7].trim())) {
                double autonomia = Double.parseDouble(dados[8].trim());
                String tipoBateria = dados[9].trim();
                double tempoCarregamento = Double.parseDouble(dados[10].trim());
                
                VeiculoEletrico veiculoEletrico = new VeiculoEletrico(
                    modelo, marca, ano, placa, chassi, 
                    autonomia, tipoBateria, tempoCarregamento
                );
                veiculoEletrico.setPreco(preco);
                veiculoEletrico.setStatus(status);
                return veiculoEletrico;
            } else {
                // Veículo convencional
                Veiculo veiculo = new Veiculo(modelo, marca, ano, placa, chassi);
                veiculo.setPreco(preco);
                veiculo.setStatus(status);
                return veiculo;
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter número na linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Carrega veículos de um arquivo PDF com tratamento de erro
     * @param caminhoPdf caminho para o arquivo PDF
     * @return lista de veículos lidos do PDF (vazia se houver erro)
     */
    public static List<Veiculo> carregarVeiculosDoPdfComTratamentoErro(String caminhoPdf) {
        try {
            return carregarVeiculosDoPdf(caminhoPdf);
        } catch (IOException e) {
            System.err.println("Erro ao ler PDF: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Valida se um arquivo PDF existe e é válido
     * @param caminhoPdf caminho para o arquivo PDF
     * @return true se o arquivo existe e é válido
     */
    public static boolean validarArquivoPdf(String caminhoPdf) {
        if (caminhoPdf == null || caminhoPdf.trim().isEmpty()) {
            return false;
        }
        
        File arquivo = new File(caminhoPdf);
        if (!arquivo.exists() || !arquivo.isFile()) {
            return false;
        }
        
        if (!caminhoPdf.toLowerCase().endsWith(".pdf")) {
            return false;
        }
        
        try (PDDocument documento = PDDocument.load(arquivo)) {
            return documento.getNumberOfPages() > 0;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Obtém informações sobre o arquivo PDF
     * @param caminhoPdf caminho para o arquivo PDF
     * @return string com informações do arquivo ou mensagem de erro
     */
    public static String obterInformacoesPdf(String caminhoPdf) {
        try (PDDocument documento = PDDocument.load(new File(caminhoPdf))) {
            return String.format("Arquivo: %s\nPáginas: %d\nTamanho: %d bytes", 
                caminhoPdf, 
                documento.getNumberOfPages(),
                new File(caminhoPdf).length()
            );
        } catch (IOException e) {
            return "Erro ao obter informações do PDF: " + e.getMessage();
        }
    }
}
