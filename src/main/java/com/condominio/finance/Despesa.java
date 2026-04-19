package com.condominio.finance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Despesa {

    protected int id;
    protected String descricao;
    protected float valor; // VALOR TOTAL
    protected LocalDate dataVencimento;
    protected String status;
    protected String tipo;
    protected LocalDateTime dataCriacao;

    //Setters


    public void setValor(float valor) {
        this.valor = valor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    //Getters


    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public float getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
