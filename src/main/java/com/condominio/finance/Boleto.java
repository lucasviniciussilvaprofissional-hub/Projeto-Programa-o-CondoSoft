package com.condominio.finance;
import java.time.LocalDate;
public class Boleto {
    private int id;
    private String codigoBarras;
    private float valor;
    private LocalDate dataVencimento;
    private String status;
    private int despesaId;

    public int getId() {
        return id;
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

    public int getDespesaId() {
        return despesaId;
    }

    public void setDespesaId(int despesaId) {
        this.despesaId = despesaId;
    }
}
