package com.mycompany.projetoblade.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa um funcionário no sistema.
 * Aplicando SRP - responsável apenas por representar dados de um funcionário.
 */
public class Funcionario {
    private int id;
    private String matricula;
    private LocalDate dataAdmissao;
    private Usuario usuario;

    // Construtor padrão
    public Funcionario() {}

    // Construtor com parâmetros principais
    public Funcionario(String matricula, LocalDate dataAdmissao, Usuario usuario) {
        this.matricula = matricula;
        this.dataAdmissao = dataAdmissao;
        this.usuario = usuario;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", matricula='" + matricula + '\'' +
                ", dataAdmissao=" + dataAdmissao +
                ", usuario=" + usuario +
                '}';
    }
}
