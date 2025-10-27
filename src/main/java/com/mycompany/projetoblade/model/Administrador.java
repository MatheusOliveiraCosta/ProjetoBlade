package com.mycompany.projetoblade.model;

import java.time.LocalDate;

/**
 * Classe que representa um administrador no sistema.
 * Aplicando OCP - extensão de Funcionario sem modificar a classe base.
 */
public class Administrador extends Funcionario {
    private String nivelAcesso;

    // Construtor padrão
    public Administrador() {}

    // Construtor com parâmetros principais
    public Administrador(String matricula, LocalDate dataAdmissao, Usuario usuario, String nivelAcesso) {
        super(matricula, dataAdmissao, usuario);
        this.nivelAcesso = nivelAcesso;
    }

    // Getters e Setters
    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "nivelAcesso='" + nivelAcesso + '\'' +
                "} " + super.toString();
    }
}
