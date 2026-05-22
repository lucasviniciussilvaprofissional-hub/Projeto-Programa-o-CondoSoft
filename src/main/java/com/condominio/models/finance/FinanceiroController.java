package com.condominio.models.finance;

import java.util.ArrayList;
import java.util.List;

import com.condominio.enums.StatusBoleto;
import com.condominio.enums.StatusDespesa;

import com.condominio.models.finance.Boleto;
import com.condominio.models.finance.Despesa;


public class FinanceiroController {

    private List<Boleto> boletos;
    private List<Despesa> despesas;

    public FinanceiroController() {

        this.boletos = new ArrayList<>();
        this.despesas = new ArrayList<>();


    }

    // =========================
    // BOLETOS
    // =========================

    public void adicionarBoleto(Boleto boleto) {

        boletos.add(boleto);

    }

    public void pagarBoleto(Boleto boleto) {

        boleto.setStatus(StatusBoleto.PAGO);

    }

    public List<Boleto> listarBoletos() {

        return boletos;

    }

    public List<Boleto> listarBoletosPendentes() {

        List<Boleto> pendentes = new ArrayList<>();

        for (Boleto boleto : boletos) {

            if (boleto.status == StatusBoleto.PENDENTE) {

                pendentes.add(boleto);

            }

        }

        return pendentes;

    }

    public double calcularValorTotalBoletos() {

        double total = 0;

        for (Boleto boleto : boletos) {

            total += boleto.getValor();

        }

        return total;

    }

    // =========================
    // DESPESAS
    // =========================

    public void adicionarDespesa(Despesa despesa) {

        despesas.add(despesa);

    }

    public List<Despesa> listarDespesasPendentes() {

        List<Despesa> pendentes = new ArrayList<>();

        for (Despesa despesa : despesas) {

            if (despesa.getStatus() == StatusDespesa.PENDENTE) {

                pendentes.add(despesa);

            }

        }

        return pendentes;

    }

    public double calcularTotalDespesas() {

        double total = 0;

        for (Despesa despesa : despesas) {

            total += despesa.getValor();

        }

        return total;

    }

    // =========================
    // MULTA
    // =========================

    public double calcularMulta(Boleto boleto) {

        return boleto.getValor() * 0.02;

    }

}
