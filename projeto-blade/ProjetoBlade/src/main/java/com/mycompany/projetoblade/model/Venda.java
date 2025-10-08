package com.mycompany.projetoblade.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa uma venda no sistema.
 * Aplicando SRP - responsável apenas por representar dados de uma venda.
 */
public class Venda {
    private int idVenda;
    private LocalDate dataVenda;
    private double valorFinal;
    private Cliente cliente;
    private Veiculo veiculo;
    private Pagamento pagamento;

    // Construtor padrão
    public Venda() {}

    // Construtor com parâmetros principais
    public Venda(LocalDate dataVenda, double valorFinal, Cliente cliente, Veiculo veiculo) {
        this.dataVenda = dataVenda;
        this.valorFinal = valorFinal;
        this.cliente = cliente;
        this.veiculo = veiculo;
    }

    // Getters e Setters
    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return idVenda == venda.idVenda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenda);
    }

    @Override
    public String toString() {
        return "Venda{" +
                "idVenda=" + idVenda +
                ", dataVenda=" + dataVenda +
                ", valorFinal=" + valorFinal +
                ", cliente=" + cliente +
                ", veiculo=" + veiculo +
                '}';
    }
}
