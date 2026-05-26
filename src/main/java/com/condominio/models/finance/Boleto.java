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

    // =========================
    // CONSTRUTOR
    // =========================

    public Boleto(int id,
                  String codigoBarras,
                  float valor,
                  String competencia,
                  LocalDate dataVencimento,
                  StatusBoleto status) {

        setId(id);
        setCodigoBarras(codigoBarras);
        setValor(valor);
        setCompetencia(competencia);
        setDataVencimento(dataVencimento);
        setStatus(status);

    }

    // =========================
    // GETTERS E SETTERS
    // =========================

    public int getId() {
        return id;
    }

    public void setId(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException("ID inválido.");

        }

        this.id = id;

    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {

        if (codigoBarras == null || codigoBarras.isEmpty()) {

            throw new IllegalArgumentException("Código de barras inválido.");

        }

        this.codigoBarras = codigoBarras;

    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {

        if (valor <= 0) {

            throw new IllegalArgumentException("Valor inválido.");

        }

        this.valor = valor;

    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {

        if (competencia == null || competencia.isEmpty()) {

            throw new IllegalArgumentException("Competência inválida.");

        }

        this.competencia = competencia;

    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {

        if (dataVencimento == null) {

            throw new IllegalArgumentException("Data inválida.");

        }

        this.dataVencimento = dataVencimento;

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

    public StatusBoleto getStatus() {
        return status;
    }

    public void setStatus(StatusBoleto status) {
        this.status = status;
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    public void pagarBoleto() {

        if (status == StatusBoleto.PAGO) {

            throw new IllegalArgumentException("Boleto já pago.");

        }

        status = StatusBoleto.PAGO;

    }

    public void cancelarBoleto() {

        if (status == StatusBoleto.PAGO) {

            throw new IllegalArgumentException("Não é possível cancelar boleto pago.");

        }

        status = StatusBoleto.CANCELADO;

    }

    public double calcularMulta() {

        if (status == StatusBoleto.VENCIDO) {

            return valor * 0.02;

        }

        return 0;

    }

    public boolean boletoVencido() {

        return LocalDate.now().isAfter(dataVencimento);

    }

}