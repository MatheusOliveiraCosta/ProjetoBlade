package com.mycompany.projetoblade.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa um cliente no sistema.
 * Aplicando SRP - responsável apenas por representar dados de um cliente.
 */
public class Cliente {
    private int id;
    private String endereco;
    private String cpf;
    private String celular;
    private Usuario usuario;
    private LocalDate dataNascimento;

    // Construtor padrão
    public Cliente() {}

    // Construtor com parâmetros principais
    public Cliente(String endereco, String cpf, Usuario usuario) {
        this.endereco = endereco;
        this.cpf = cpf;
        this.usuario = usuario;
    }

    public Cliente(String endereco, String cpf, String celular, Usuario usuario) {
        this.endereco = endereco;
        this.cpf = cpf;
        this.celular = celular;
        this.usuario = usuario;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", endereco='" + endereco + '\'' +
                ", cpf='" + cpf + '\'' +
                ", usuario=" + usuario +
                '}';
    }

    // Antigo método removido — agora usamos LocalDate getDataNascimento/setDataNascimento
}
