package com.mycompany.projetoblade.model;

import java.util.Objects;

/**
 * Classe que representa um pagamento no sistema.
 * Aplicando SRP - responsável apenas por representar dados de um pagamento.
 */
public class Pagamento {
    private int idPagamento;
    private String metodo;
    private String status;

    // Construtor padrão
    public Pagamento() {}

    // Construtor com parâmetros principais
    public Pagamento(String metodo, String status) {
        this.metodo = metodo;
        this.status = status;
    }

    // Getters e Setters
    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
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
        Pagamento pagamento = (Pagamento) o;
        return idPagamento == pagamento.idPagamento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPagamento);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "idPagamento=" + idPagamento +
                ", metodo='" + metodo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
