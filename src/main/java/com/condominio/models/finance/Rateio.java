package com.condominio.models.finance;

import com.condominio.models.moradia.Unidade;

public class Rateio {

    private int id;
    private float valorRateado;
    private String competencia;
    private Despesa despesa;
    private Unidade unidade;


    public Rateio() {
    }

    // getters e setters


    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

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
