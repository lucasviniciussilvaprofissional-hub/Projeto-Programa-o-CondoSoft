package com.condominio.models.finance;

import java.util.ArrayList;
import java.util.List;

import com.condominio.enums.StatusBoleto;
import com.condominio.enums.StatusDespesa;

public class FinanceiroController {

    private List<Boleto> boletos;
    private List<Despesa> despesas;

    // =========================
    // CONSTRUTOR
    // =========================

    public FinanceiroController() {

        this.boletos = new ArrayList<>();
        this.despesas = new ArrayList<>();

    }

    // =========================
    // BOLETOS
    // =========================

    public void adicionarBoleto(Boleto boleto) {

        if (boleto == null) {

            throw new IllegalArgumentException("Boleto inválido.");

        }

        if (boleto.getValor() <= 0) {

            throw new IllegalArgumentException("Valor do boleto inválido.");

        }

        boletos.add(boleto);

    }

    public void pagarBoleto(Boleto boleto) {

        if (boleto == null) {

            throw new IllegalArgumentException("Boleto inexistente.");

        }

        if (boleto.getStatus() == StatusBoleto.PAGO) {

            throw new IllegalArgumentException("Boleto já pago.");

        }

        boleto.setStatus(StatusBoleto.PAGO);

    }

    public void cancelarBoleto(Boleto boleto) {

        if (boleto == null) {

            throw new IllegalArgumentException("Boleto inválido.");

        }

        if (boleto.getStatus() == StatusBoleto.PAGO) {

            throw new IllegalArgumentException("Não é possível cancelar boleto pago.");

        }

        boleto.setStatus(StatusBoleto.CANCELADO);

    }

    public List<Boleto> listarBoletos() {

        return boletos;

    }

    public List<Boleto> listarBoletosPendentes() {

        List<Boleto> pendentes = new ArrayList<>();

        for (Boleto boleto : boletos) {

            if (boleto.getStatus() == StatusBoleto.PENDENTE) {

                pendentes.add(boleto);

            }

        }

        return pendentes;

    }

    public List<Boleto> listarBoletosVencidos() {

        List<Boleto> vencidos = new ArrayList<>();

        for (Boleto boleto : boletos) {

            if (boleto.getStatus() == StatusBoleto.VENCIDO) {

                vencidos.add(boleto);

            }

        }

        return vencidos;

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

        if (despesa == null) {

            throw new IllegalArgumentException("Despesa inválida.");

        }

        if (despesa.getValor() <= 0) {

            throw new IllegalArgumentException("Valor da despesa inválido.");

        }

        despesas.add(despesa);

    }

    public void pagarDespesa(Despesa despesa) {

        if (despesa == null) {

            throw new IllegalArgumentException("Despesa inválida.");

        }

        if (despesa.getStatus() == StatusDespesa.PAGA) {

            throw new IllegalArgumentException("Despesa já paga.");

        }

        despesa.setStatus(StatusDespesa.PAGA);

    }

    public List<Despesa> listarDespesas() {

        return despesas;

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

        if (boleto == null) {

            throw new IllegalArgumentException("Boleto inválido.");

        }

        if (boleto.getStatus() == StatusBoleto.VENCIDO) {

            return boleto.getValor() * 0.02;

        }

        return 0;

    }

    // =========================
    // RELATÓRIOS
    // =========================

    public int quantidadeBoletosPendentes() {

        int quantidade = 0;

        for (Boleto boleto : boletos) {

            if (boleto.getStatus() == StatusBoleto.PENDENTE) {

                quantidade++;

            }

        }

        return quantidade;

    }

    public int quantidadeDespesasPendentes() {

        int quantidade = 0;

        for (Despesa despesa : despesas) {

            if (despesa.getStatus() == StatusDespesa.PENDENTE) {

                quantidade++;

            }

        }

        return quantidade;

    }

}