package com.mycompany.projetoblade.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa uma manutenção no sistema.
 * Aplicando SRP - responsável apenas por representar dados de uma manutenção.
 */
public class Manutencao {
    private int idManutencao;
    private LocalDate dataAgendamento;
    private String descricao;
    private String status;
    private Veiculo veiculo;
    private Funcionario funcionario;
    private LaudoTecnico laudoTecnico;
    private Orcamento orcamento;

    // Construtor padrão
    public Manutencao() {}

    // Construtor com parâmetros principais
    public Manutencao(LocalDate dataAgendamento, String descricao, String status, Veiculo veiculo, Funcionario funcionario) {
        this.dataAgendamento = dataAgendamento;
        this.descricao = descricao;
        this.status = status;
        this.veiculo = veiculo;
        this.funcionario = funcionario;
    }

    // Getters e Setters
    public int getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(int idManutencao) {
        this.idManutencao = idManutencao;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public LaudoTecnico getLaudoTecnico() {
        return laudoTecnico;
    }

    public void setLaudoTecnico(LaudoTecnico laudoTecnico) {
        this.laudoTecnico = laudoTecnico;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manutencao that = (Manutencao) o;
        return idManutencao == that.idManutencao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idManutencao);
    }

    @Override
    public String toString() {
        return "Manutencao{" +
                "idManutencao=" + idManutencao +
                ", dataAgendamento=" + dataAgendamento +
                ", descricao='" + descricao + '\'' +
                ", status='" + status + '\'' +
                ", veiculo=" + veiculo +
                ", funcionario=" + funcionario +
                '}';
    }
}
