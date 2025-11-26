package com.mycompany.projetoblade.model;

import java.util.Objects;

/**
 * Classe que representa um veículo no sistema.
 * Aplicando SRP - responsável apenas por representar dados de um veículo.
 */
public class Veiculo {
    private int idVeiculo;
    private String modelo;
    private String placa;
    private int ano;
    private String chassi;
    private String marca;
    private String status;
    private double preco;
    private Cliente dono;

    // Construtor padrão
    public Veiculo() {}

    // Construtor com parâmetros principais
    public Veiculo(String modelo, String marca, int ano, String placa, String chassi) {
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.placa = placa;
        this.chassi = chassi;
        this.status = "DISPONIVEL";
    }

    // Getters e Setters
    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Cliente getDono() {
        return dono;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return idVeiculo == veiculo.idVeiculo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVeiculo);
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "idVeiculo=" + idVeiculo +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", ano=" + ano +
                ", placa='" + placa + '\'' +
                ", status='" + status + '\'' +
                ", preco=" + preco +
                '}';
    }
}
