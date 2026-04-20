package com.condominio.finance;
import com.condominio.model.Unidade;

import java.time.LocalDate;

public class Boleto {
    private int id;
    private String codigoBarras;
    private float valor;
    private LocalDate dataVencimento;
    private String status;
    private Despesa despesa;
    private Unidade unidade;

    public int getId() {
        return id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Despesa getDespesa() {
        return despesa;
    }

    public void setDespesa(Despesa despesa) {
        this.despesa = despesa;
    }
}
