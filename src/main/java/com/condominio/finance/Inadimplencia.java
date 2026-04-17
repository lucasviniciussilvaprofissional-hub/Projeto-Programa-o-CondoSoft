package com.condominio.finance;

public class Inadimplencia {

    private int id;
    private float valorDevido;
    private int diasAtraso;
    private String status;

    private int boletoId;
    private int unidadeId;

    public Inadimplencia() {
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

    public int getBoletoId() {
        return boletoId;
    }

    public void setBoletoId(int boletoId) {
        this.boletoId = boletoId;
    }

    public int getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(int unidadeId) {
        this.unidadeId = unidadeId;
    }
}