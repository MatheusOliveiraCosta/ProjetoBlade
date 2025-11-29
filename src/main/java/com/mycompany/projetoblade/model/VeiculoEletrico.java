package com.mycompany.projetoblade.model;

/**
 * Representa um veículo elétrico — extensão simples de `Veiculo`.
 */
public class VeiculoEletrico extends Veiculo {
    private double autonomia;
    private String tipoBateria;
    private double tempoCarregamento;

    public VeiculoEletrico() {}

    public VeiculoEletrico(String modelo, String marca, int ano, String placa, String chassi,
                           double autonomia, String tipoBateria, double tempoCarregamento) {
        super(modelo, marca, ano, placa, chassi);
        this.autonomia = autonomia;
        this.tipoBateria = tipoBateria;
        this.tempoCarregamento = tempoCarregamento;
    }

    public double getAutonomia() { return autonomia; }
    public void setAutonomia(double autonomia) { this.autonomia = autonomia; }

    public String getTipoBateria() { return tipoBateria; }
    public void setTipoBateria(String tipoBateria) { this.tipoBateria = tipoBateria; }

    public double getTempoCarregamento() { return tempoCarregamento; }
    public void setTempoCarregamento(double tempoCarregamento) { this.tempoCarregamento = tempoCarregamento; }

    @Override
    public String toString() {
        return "VeiculoEletrico{" +
                "modelo='" + getModelo() + '\'' +
                ", marca='" + getMarca() + '\'' +
                ", autonomia=" + autonomia +
                ", tipoBateria='" + tipoBateria + '\'' +
                '}';
    }
}
