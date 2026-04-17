package com.condominio.relatorio;

public class Balancete {

    private int id;
    private float totalReceitas;
    private float totalDespesas;
    private float saldoFinal;

    private String periodo; // ex: "2026-04"

    public Balancete() {
    }

    // getters e setters

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
}