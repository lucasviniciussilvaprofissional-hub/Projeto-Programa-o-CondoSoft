package com.condominio.finance;

public class Rateio {

    private int id;
    private float valorTotal;
    private int despesaId;
    private int unidadeId;

    public Rateio() {
    }

    // getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getDespesaId() {
        return despesaId;
    }

    public void setDespesaId(int despesaId) {
        this.despesaId = despesaId;
    }

    public int getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(int unidadeId) {
        this.unidadeId = unidadeId;
    }
}
