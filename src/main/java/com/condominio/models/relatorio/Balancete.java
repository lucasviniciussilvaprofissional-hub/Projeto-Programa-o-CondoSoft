package com.condominio.models.relatorio;

import java.util.List;
import com.condominio.models.finance.Pagamento;
import com.condominio.models.finance.Despesa;

public class Balancete {

    private int id;
    private float totalReceitas;
    private float totalDespesas;
    private float saldoFinal;
    private String periodo;

    private List<Pagamento> pagamentos;
    private List<Despesa> despesas;

    public Balancete() {
    }

    public Balancete(int id, float totalReceitas, float totalDespesas, String periodo) {
        this.id = id;
        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.saldoFinal = totalReceitas - totalDespesas;
        this.periodo = periodo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotalReceitas() {
        return totalReceitas;
    }

    public void setTotalReceitas(float totalReceitas) {
        this.totalReceitas = totalReceitas;
    }

    public float getTotalDespesas() {
        return totalDespesas;
    }

    public void setTotalDespesas(float totalDespesas) {
        this.totalDespesas = totalDespesas;
    }

    public float getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(float saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public List<Despesa> getDespesas() {
        return despesas;
    }

    public void setDespesas(List<Despesa> despesas) {
        this.despesas = despesas;
    }
}