package com.condominio.models.disciplina;

import java.time.LocalDateTime;

public class Ocorrencia {

    private int id;
    private String descricao;
    private String tipo; // BARULHO, ELEVADOR, SEGURANCA
    private String status; // ABERTA, EM_ANALISE, RESOLVIDA
    private LocalDateTime dataRegistro;
    private String unidade;

    public Ocorrencia() {
        this.dataRegistro = LocalDateTime.now();
        this.status = "ABERTA";
    }

    // getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
    public void setDataRegistro(LocalDateTime dataRegistro){
        this.dataRegistro = dataRegistro;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}