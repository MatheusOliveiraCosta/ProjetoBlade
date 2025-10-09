package com.mycompany.projetoblade.model;

import java.time.LocalDate;

/**
 * Classe que representa um mecânico no sistema.
 * Aplicando OCP - extensão de Funcionario sem modificar a classe base.
 */
public class Mecanico extends Funcionario {
    private String especialidade;

    // Construtor padrão
    public Mecanico() {}

    // Construtor com parâmetros principais
    public Mecanico(String matricula, LocalDate dataAdmissao, Usuario usuario, String especialidade) {
        super(matricula, dataAdmissao, usuario);
        this.especialidade = especialidade;
    }

    // Getters e Setters
    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return "Mecanico{" +
                "especialidade='" + especialidade + '\'' +
                "} " + super.toString();
    }
}
