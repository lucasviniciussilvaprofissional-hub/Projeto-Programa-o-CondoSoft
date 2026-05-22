package com.condominio.models.finance;
import com.condominio.enums.StatusBoleto;
import com.condominio.models.moradia.Unidade;

import java.time.LocalDate;

public class Boleto {
    private int id;
    private String codigoBarras;
    private float valor;
    private String competencia;
    private LocalDate dataVencimento;
    private Despesa despesa;
    private Unidade unidade;
    private StatusBoleto status;

    Boleto(int id, String codigoBarras, float valor, String competencia, LocalDate dataVencimento, StatusBoleto status)
    {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.valor = valor;
        this.competencia = competencia;
        this.dataVencimento = dataVencimento;
        this.status = status;
    }


    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

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

    public StatusBoleto Boleto(StatusBoleto status) {
        return status;
    }

    public void setStatus(StatusBoleto status) {

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
