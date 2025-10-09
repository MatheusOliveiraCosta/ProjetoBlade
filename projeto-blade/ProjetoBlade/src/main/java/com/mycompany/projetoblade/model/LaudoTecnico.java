package com.mycompany.projetoblade.model;

import java.util.Objects;

/**
 * Classe que representa um laudo técnico no sistema.
 * Aplicando SRP - responsável apenas por representar dados de um laudo técnico.
 */
public class LaudoTecnico {
    private int idLaudo;
    private String descricaoServico;

    // Construtor padrão
    public LaudoTecnico() {}

    // Construtor com parâmetros principais
    public LaudoTecnico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    // Getters e Setters
    public int getIdLaudo() {
        return idLaudo;
    }

    public void setIdLaudo(int idLaudo) {
        this.idLaudo = idLaudo;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaudoTecnico that = (LaudoTecnico) o;
        return idLaudo == that.idLaudo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLaudo);
    }

    @Override
    public String toString() {
        return "LaudoTecnico{" +
                "idLaudo=" + idLaudo +
                ", descricaoServico='" + descricaoServico + '\'' +
                '}';
    }
}
