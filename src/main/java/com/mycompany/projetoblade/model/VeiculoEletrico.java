package com.mycompany.projetoblade.model;

/**
 * Classe que representa um veículo elétrico no sistema.
 * Aplicando OCP - extensão de Veiculo sem modificar a classe base.
 */
public class VeiculoEletrico extends Veiculo {
    private double autonomiaBateria;
    private String tipoBateria;
    private double tempoCarregamento;

    // Construtor padrão
    public VeiculoEletrico() {}

    // Construtor com parâmetros principais
    public VeiculoEletrico(String modelo, String marca, int ano, String placa, String chassi, 
                          double autonomiaBateria, String tipoBateria, double tempoCarregamento) {
        super(modelo, marca, ano, placa, chassi);
        this.autonomiaBateria = autonomiaBateria;
        this.tipoBateria = tipoBateria;
        this.tempoCarregamento = tempoCarregamento;
    }

    // Getters e Setters
    public double getAutonomiaBateria() {
        return autonomiaBateria;
    }

    public void setAutonomiaBateria(double autonomiaBateria) {
        this.autonomiaBateria = autonomiaBateria;
    }

    public String getTipoBateria() {
        return tipoBateria;
    }

    public void setTipoBateria(String tipoBateria) {
        this.tipoBateria = tipoBateria;
    }

    public double getTempoCarregamento() {
        return tempoCarregamento;
    }

    public void setTempoCarregamento(double tempoCarregamento) {
        this.tempoCarregamento = tempoCarregamento;
    }

    @Override
    public String toString() {
        return "VeiculoEletrico{" +
                "autonomiaBateria=" + autonomiaBateria +
                ", tipoBateria='" + tipoBateria + '\'' +
                ", tempoCarregamento=" + tempoCarregamento +
                "} " + super.toString();
    }
}
