package com.condominio.models.disciplina;

import java.time.LocalDateTime;

import com.condominio.enums.StatusOcorrencia;
import com.condominio.enums.TipoOcorrencia;

public class Ocorrencia {

    private int id;
    private String descricao;
    private TipoOcorrencia tipo; // BARULHO, ELEVADOR, SEGURANCA
    private StatusOcorrencia status; // ABERTA, EM_ANALISE, RESOLVIDA
    private LocalDateTime dataRegistro;
    private String unidade;

    public Ocorrencia() {
        this.dataRegistro = LocalDateTime.now();
        this.status = StatusOcorrencia.valueOf("ABERTA");
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

    public TipoOcorrencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoOcorrencia tipo) {
        this.tipo = tipo;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
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