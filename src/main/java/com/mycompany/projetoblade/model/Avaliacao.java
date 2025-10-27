package com.mycompany.projetoblade.model;

import java.util.Objects;

/**
 * Classe que representa uma avaliação no sistema.
 * Aplicando SRP - responsável apenas por representar dados de uma avaliação.
 */
public class Avaliacao {
    private int idAvaliacao;
    private int nota;
    private String comentario;
    private Cliente cliente;

    // Construtor padrão
    public Avaliacao() {}

    // Construtor com parâmetros principais
    public Avaliacao(int nota, String comentario, Cliente cliente) {
        this.nota = nota;
        this.comentario = comentario;
        this.cliente = cliente;
    }

    // Getters e Setters
    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avaliacao avaliacao = (Avaliacao) o;
        return idAvaliacao == avaliacao.idAvaliacao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAvaliacao);
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "idAvaliacao=" + idAvaliacao +
                ", nota=" + nota +
                ", comentario='" + comentario + '\'' +
                ", cliente=" + cliente +
                '}';
    }
}
