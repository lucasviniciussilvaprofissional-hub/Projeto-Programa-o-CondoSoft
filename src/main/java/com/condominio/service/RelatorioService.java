package com.condominio.service;

import com.condominio.models.finance.*;
import com.condominio.models.relatorio.Balancete;
import com.condominio.repository.interfaces.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RelatorioService {

    private final IBoletoRepository boletoRepo;
    private final IDespesaRepository despesaRepo;
    private final IPagamentoRepository pagamentoRepo;
    private final IInadimplenciaRepository inadimplenciaRepo;

    public RelatorioService(
            IBoletoRepository boletoRepo,
            IDespesaRepository despesaRepo,
            IPagamentoRepository pagamentoRepo,
            IInadimplenciaRepository inadimplenciaRepo) {

        this.boletoRepo = boletoRepo;
        this.despesaRepo = despesaRepo;
        this.pagamentoRepo = pagamentoRepo;
        this.inadimplenciaRepo = inadimplenciaRepo;
    }

    public Balancete gerarBalancete(int mes, int ano) {

        String periodo = String.format("%02d/%d", mes, ano);

        List<Pagamento> pagamentos = new ArrayList<>();
        List<Despesa> despesas = new ArrayList<>();

        float receitas = 0;
        float totalDespesas = 0;

        for (Pagamento p : pagamentoRepo.listar()) {

            if (p.getDataPagamento() == null)
                continue;

            if (p.getDataPagamento().getMonthValue() == mes &&
                    p.getDataPagamento().getYear() == ano) {

                pagamentos.add(p);
                receitas += p.getValorPago();
            }
        }

        for (Despesa d : despesaRepo.listar()) {

            if (d.getDataVencimento() == null)
                continue;

            if (d.getDataVencimento().getMonthValue() == mes &&
                    d.getDataVencimento().getYear() == ano) {

                despesas.add(d);
                totalDespesas += d.getValor();
            }
        }

        Balancete balancete =
                new Balancete(
                        1,
                        receitas,
                        totalDespesas,
                        periodo
                );

        balancete.setPagamentos(pagamentos);
        balancete.setDespesas(despesas);

        return balancete;
    }

    public double calcularInadimplenciaTotal() {

        double total = 0;

        for (Inadimplencia i : inadimplenciaRepo.listar()) {

            if ("PENDENTE".equalsIgnoreCase(i.getStatus())) {

                total += i.getValorDevido();
            }
        }

        return total;
    }

    public List<Inadimplencia> listarInadimplenciasPendentes() {

        List<Inadimplencia> lista = new ArrayList<>();

        for (Inadimplencia i : inadimplenciaRepo.listar()) {

            if ("PENDENTE".equalsIgnoreCase(i.getStatus())) {

                lista.add(i);
            }
        }

        return lista;
    }

    public String gerarPdfBalancete(
            Balancete balancete,
            String pastaDestino) throws IOException {

        String nomeArquivo =
                "Balancete_" +
                        balancete.getPeriodo().replace("/", "_")
                        + ".html";

        Path destino =
                Path.of(pastaDestino, nomeArquivo);

        Files.writeString(
                destino,
                "<html><body><h1>Balancete "
                        + balancete.getPeriodo()
                        + "</h1>"
                        + "<p>Receitas: R$ "
                        + balancete.getTotalReceitas()
                        + "</p>"
                        + "<p>Despesas: R$ "
                        + balancete.getTotalDespesas()
                        + "</p>"
                        + "<p>Saldo: R$ "
                        + balancete.getSaldoFinal()
                        + "</p>"
                        + "</body></html>",
                StandardCharsets.UTF_8
        );

        return destino.toAbsolutePath().toString();
    }

    public String exportarInadimplentesCSV(
            String pastaDestino) throws IOException {

        String nomeArquivo =
                "inadimplentes.csv";

        Path destino =
                Path.of(pastaDestino, nomeArquivo);

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "Unidade;Valor;DiasAtraso\n"
        );

        for (Inadimplencia i :
                listarInadimplenciasPendentes()) {

            csv.append(
                    i.getUnidade().getNumero()
                            + ";"
                            + i.getValorDevido()
                            + ";"
                            + i.getDiasAtraso()
                            + "\n"
            );
        }

        Files.writeString(
                destino,
                csv.toString(),
                StandardCharsets.UTF_8
        );

        return destino.toAbsolutePath().toString();
    }
}