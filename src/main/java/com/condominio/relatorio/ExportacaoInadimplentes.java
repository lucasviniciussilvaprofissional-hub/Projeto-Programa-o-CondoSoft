package com.condominio.relatorio;

import java.time.LocalDateTime;

public class ExportacaoInadimplentes {

    private int id;
    private String nomeMorador;
    private float valorDevido;
    private int diasAtraso;
    private LocalDateTime dataExportacao;

    public ExportacaoInadimplentes() {
        this.dataExportacao = LocalDateTime.now();
    }

    // getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeMorador() {
        return nomeMorador;
    }

    public void setNomeMorador(String nomeMorador) {
        this.nomeMorador = nomeMorador;
    }

    public float getValorDevido() {
        return valorDevido;
    }

    public void setValorDevido(float valorDevido) {
        this.valorDevido = valorDevido;
    }

    public int getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(int diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public LocalDateTime getDataExportacao() {
        return dataExportacao;
    }
    public void setDataExportacao (LocalDateTime dataExportacao){
        this.dataExportacao= dataExportacao;
    }
}