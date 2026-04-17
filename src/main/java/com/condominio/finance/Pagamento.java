package com.condominio.finance;

import java.time.LocalDateTime;

public class Pagamento {

    private int id;
    private float valorPago;
    private String formaPagamento;
    private LocalDateTime dataPagamento;
    private int boletoId;
    private int despesaId;

    public Pagamento() {
        this.dataPagamento = LocalDateTime.now();
    }

    // getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValorPago() {
        return valorPago;
    }

    public void setValorPago(float valorPago) {
        this.valorPago = valorPago;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public int getBoletoId() {
        return boletoId;
    }

    public void setBoletoId(int boletoId) {
        this.boletoId = boletoId;
    }

    public int getDespesaId() {
        return despesaId;
    }

    public void setDespesaId(int despesaId) {
        this.despesaId = despesaId;
    }
}