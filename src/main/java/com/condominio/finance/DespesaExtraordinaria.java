package com.condominio.finance;

public class DespesaExtraordinaria extends Despesa {

    private String motivo;

    public DespesaExtraordinaria() {
        super();
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}