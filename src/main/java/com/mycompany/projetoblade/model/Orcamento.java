package com.mycompany.projetoblade.model;

import java.util.Objects;

/**
 * Classe que representa um orçamento no sistema.
 * Aplicando SRP - responsável apenas por representar dados de um orçamento.
 */
public class Orcamento {
    private int idOrcamento;
    private double valor;
    private String detalhesServico;
    private String status;

    // Construtor padrão
    public Orcamento() {}

    // Construtor com parâmetros principais
    public Orcamento(double valor, String detalhesServico, String status) {
        this.valor = valor;
        this.detalhesServico = detalhesServico;
        this.status = status;
    }

    // Getters e Setters
    public int getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(int idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDetalhesServico() {
        return detalhesServico;
    }

    public void setDetalhesServico(String detalhesServico) {
        this.detalhesServico = detalhesServico;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orcamento orcamento = (Orcamento) o;
        return idOrcamento == orcamento.idOrcamento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrcamento);
    }

    @Override
    public String toString() {
        return "Orcamento{" +
                "idOrcamento=" + idOrcamento +
                ", valor=" + valor +
                ", detalhesServico='" + detalhesServico + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
