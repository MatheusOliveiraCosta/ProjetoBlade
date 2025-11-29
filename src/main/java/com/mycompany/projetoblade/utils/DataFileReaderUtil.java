package com.mycompany.projetoblade.utils;

import com.mycompany.projetoblade.model.Veiculo;
import com.mycompany.projetoblade.model.VeiculoEletrico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário para leitura de dados de veículos a partir de arquivos de texto.
 * Aplicando SRP - responsável apenas por extrair dados de veículos de arquivos de texto.
 */
public class DataFileReaderUtil {
    
    /**
     * Carrega veículos de um arquivo de texto
     * @param caminhoArquivo caminho para o arquivo de texto
     * @return lista de veículos lidos do arquivo
     * @throws IOException se houver erro na leitura do arquivo
     */
    public static List<Veiculo> carregarVeiculosDoArquivo(String caminhoArquivo) throws IOException {
        List<Veiculo> veiculos = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
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
     * Carrega veículos de um arquivo com tratamento de erro
     * @param caminhoArquivo caminho para o arquivo
     * @return lista de veículos lidos do arquivo (vazia se houver erro)
     */
    public static List<Veiculo> carregarVeiculosDoArquivoComTratamentoErro(String caminhoArquivo) {
        try {
            return carregarVeiculosDoArquivo(caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Valida se um arquivo existe e é válido
     * @param caminhoArquivo caminho para o arquivo
     * @return true se o arquivo existe e é válido
     */
    public static boolean validarArquivo(String caminhoArquivo) {
        if (caminhoArquivo == null || caminhoArquivo.trim().isEmpty()) {
            return false;
        }
        
        File arquivo = new File(caminhoArquivo);
        return arquivo.exists() && arquivo.isFile() && arquivo.canRead();
    }
    
    /**
     * Obtém informações sobre o arquivo
     * @param caminhoArquivo caminho para o arquivo
     * @return string com informações do arquivo ou mensagem de erro
     */
    public static String obterInformacoesArquivo(String caminhoArquivo) {
        try {
            File arquivo = new File(caminhoArquivo);
            return String.format("Arquivo: %s\nTamanho: %d bytes\nÚltima modificação: %s", 
                caminhoArquivo, 
                arquivo.length(),
                new java.util.Date(arquivo.lastModified())
            );
        } catch (Exception e) {
            return "Erro ao obter informações do arquivo: " + e.getMessage();
        }
    }
}
