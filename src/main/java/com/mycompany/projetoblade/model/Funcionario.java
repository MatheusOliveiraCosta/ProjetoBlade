package com.mycompany.projetoblade.model;

import java.time.LocalDate;

/**
 * Classe que representa um funcionário no sistema.
 * Aplicando SRP - responsável apenas por representar dados de um funcionário.
 */
public class Funcionario extends Usuario {
    private String matricula;
    private LocalDate dataAdmissao;
    
    // NOVO CAMPO: O "Crachá"
    // Usamos protected para que as classes filhas (Mecânico/Admin) possam acessá-lo direto
    protected String tipo; 

    public Funcionario() {}

    public Funcionario(String matricula, LocalDate dataAdmissao, Usuario usuario) {
        super(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
        this.matricula = matricula;
        this.dataAdmissao = dataAdmissao;
    }

    // Getters and setters to ensure fields are used
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

    // Getter para o menu ler
    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + getNome() + '\'' +
                ", matricula='" + matricula + '\'' +
                ", dataAdmissao=" + dataAdmissao +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
