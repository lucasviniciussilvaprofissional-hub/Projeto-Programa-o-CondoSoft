package com.condominio.models.finance;

public class DespesaFixa extends Despesa {

    private String periodicidade; // MENSAL, ANUAL

    public DespesaFixa() {
        super();
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }
}