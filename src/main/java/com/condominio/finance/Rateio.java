package com.condominio.finance;

import com.condominio.model.Unidade;

public class Rateio {

    private int id;
    private float valorRateado;
    private Despesa despesa;
    private Unidade unidade;


    public Rateio() {
    }

    // getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValorRateado() {
        return valorRateado;
    }

    public void setValorRateado(float valorRateado) {
        this.valorRateado = valorRateado;
    }

    public Despesa getDespesa() {
        return despesa;
    }

    public void setDespesa(Despesa despesa) {
        this.despesa = despesa;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
}
