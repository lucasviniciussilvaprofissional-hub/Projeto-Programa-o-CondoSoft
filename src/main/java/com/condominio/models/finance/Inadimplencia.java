package com.condominio.models.finance;

import com.condominio.models.moradia.Unidade;

public class Inadimplencia {

    private int id;
    private float valorDevido;
    private int diasAtraso;
    private String status;
    private Boleto boleto;
    private Unidade unidade;

    public Inadimplencia(int id,
                         float valorDevido,
                         int diasAtraso,
                         Boleto boleto,
                         Unidade unidade) {

        this.id = id;
        this.valorDevido = valorDevido;
        this.diasAtraso = diasAtraso;
        this.boleto = boleto;
        this.unidade = unidade;
        this.status = "PENDENTE";
    }
    // getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public Boleto getBoleto() {
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}