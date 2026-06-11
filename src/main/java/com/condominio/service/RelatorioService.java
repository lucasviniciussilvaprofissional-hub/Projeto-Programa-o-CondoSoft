package com.condominio.service;

import com.condominio.models.finance.Despesa;
import com.condominio.models.finance.Inadimplencia;
import com.condominio.models.finance.Pagamento;
import com.condominio.models.relatorio.Balancete;
import com.condominio.repository.interfaces.IBoletoRepository;
import com.condominio.repository.interfaces.IDespesaRepository;
import com.condominio.repository.interfaces.IInadimplenciaRepository;
import com.condominio.repository.interfaces.IPagamentoRepository;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioService {

    private final IBoletoRepository        boletoRepo;
    private final IDespesaRepository       despesaRepo;
    private final IPagamentoRepository     pagamentoRepo;
    private final IInadimplenciaRepository inadimplenciaRepo;

    public RelatorioService(
            IBoletoRepository boletoRepo,
            IDespesaRepository despesaRepo,
            IPagamentoRepository pagamentoRepo,
            IInadimplenciaRepository inadimplenciaRepo) {

        this.boletoRepo        = boletoRepo;
        this.despesaRepo       = despesaRepo;
        this.pagamentoRepo     = pagamentoRepo;
        this.inadimplenciaRepo = inadimplenciaRepo;
    }

    // =========================================================================
    // Geracao do Balancete (logica de dados - sem mudancas)
    // =========================================================================

    public Balancete gerarBalancete(int mes, int ano) {

        String periodo = String.format("%02d/%d", mes, ano);

        List<Pagamento> pagamentos    = new ArrayList<>();
        List<Despesa>   despesas      = new ArrayList<>();
        float           receitas      = 0;
        float           totalDespesas = 0;

        for (Pagamento p : pagamentoRepo.listar()) {
            if (p.getDataPagamento() == null) continue;
            if (p.getDataPagamento().getMonthValue() == mes
                    && p.getDataPagamento().getYear() == ano) {
                pagamentos.add(p);
                receitas += p.getValorPago();
            }
        }

        for (Despesa d : despesaRepo.listar()) {
            if (d.getDataVencimento() == null) continue;
            if (d.getDataVencimento().getMonthValue() == mes
                    && d.getDataVencimento().getYear() == ano) {
                despesas.add(d);
                totalDespesas += d.getValor();
            }
        }

        Balancete balancete = new Balancete(1, receitas, totalDespesas, periodo);
        balancete.setPagamentos(pagamentos);
        balancete.setDespesas(despesas);
        return balancete;
    }

    // =========================================================================
    // REQ14 - Gerar PDF via JasperReports
    // =========================================================================

    /**
     * Gera o balancete em PDF usando JasperReports.
     * O template balancete.jrxml deve estar em src/main/resources/relatorios/
     *
     * @param balancete    objeto populado por gerarBalancete()
     * @param pastaDestino pasta onde o PDF sera salvo
     * @return caminho absoluto do arquivo gerado
     */
    public String gerarPdfBalancete(Balancete balancete, String pastaDestino) throws Exception {

        // --- 1. Carrega e compila o template JRXML do classpath --------------
        InputStream jrxmlStream =
                getClass().getResourceAsStream(
                        "/com/condominio/relatorio/balancete.jrxml");

        if (jrxmlStream == null) {
            throw new IllegalStateException(
                    "Template nao encontrado: /relatorio/balancete.jrxml\n"
                            + "Verifique se o arquivo esta em src/main/resources/relatorio/");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        // --- 2. Monta os parametros do cabecalho -----------------------------
        Map<String, Object> params = new HashMap<>();

        params.put("PERIODO",        balancete.getPeriodo());
        params.put("DATA_GERACAO",   LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        params.put("TOTAL_RECEITAS", moeda(balancete.getTotalReceitas()));
        params.put("TOTAL_DESPESAS", moeda(balancete.getTotalDespesas()));
        params.put("SALDO_FINAL",    moeda(balancete.getSaldoFinal()));
        params.put("COR_SALDO",      balancete.getSaldoFinal() >= 0 ? "verde" : "vermelho");

        // --- 3. Monta o datasource com as linhas de lancamentos --------------
        List<LancamentoRow> linhas = montarLinhas(balancete);

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(linhas.isEmpty()
                        ? List.of(new LancamentoRow("-", "Sem lancamentos", "-", "-", "-", "-"))
                        : linhas);

        // --- 4. Preenche o relatorio -----------------------------------------
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, params, dataSource);

        // --- 5. Exporta para PDF ---------------------------------------------
        String nomeArquivo = "Balancete_"
                + balancete.getPeriodo().replace("/", "_")
                + ".pdf";

        Path destino = Path.of(pastaDestino, nomeArquivo);

        JasperExportManager.exportReportToPdfFile(
                jasperPrint,
                destino.toAbsolutePath().toString());

        return destino.toAbsolutePath().toString();
    }

    // =========================================================================
    // REQ15 - Exportar inadimplentes em CSV
    // =========================================================================

    public String exportarInadimplentesCSV(String pastaDestino) throws IOException {
        Path destino = Path.of(pastaDestino, "inadimplentes.csv");
        StringBuilder csv = new StringBuilder("Unidade;Valor;DiasAtraso\n");
        for (Inadimplencia i : listarInadimplenciasPendentes()) {
            csv.append(i.getUnidade().getNumero())
                    .append(";").append(i.getValorDevido())
                    .append(";").append(i.getDiasAtraso())
                    .append("\n");
        }
        Files.writeString(destino, csv.toString(), StandardCharsets.UTF_8);
        return destino.toAbsolutePath().toString();
    }

    // =========================================================================
    // Consultas auxiliares
    // =========================================================================

    public double calcularInadimplenciaTotal() {
        double total = 0;
        for (Inadimplencia i : inadimplenciaRepo.listar())
            if ("PENDENTE".equalsIgnoreCase(i.getStatus()))
                total += i.getValorDevido();
        return total;
    }

    public List<Inadimplencia> listarInadimplenciasPendentes() {
        List<Inadimplencia> lista = new ArrayList<>();
        for (Inadimplencia i : inadimplenciaRepo.listar())
            if ("PENDENTE".equalsIgnoreCase(i.getStatus()))
                lista.add(i);
        return lista;
    }

    // =========================================================================
    // Helpers privados
    // =========================================================================

    /**
     * Constroi a lista de linhas para o datasource do JasperReports
     * a partir dos pagamentos e despesas do balancete.
     */
    private List<LancamentoRow> montarLinhas(Balancete balancete) {

        List<LancamentoRow> linhas = new ArrayList<>();
        double saldoAcum = 0;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (balancete.getPagamentos() != null) {
            for (Pagamento p : balancete.getPagamentos()) {
                saldoAcum += p.getValorPago();

                String data = p.getDataPagamento() != null
                        ? p.getDataPagamento().toLocalDate().format(fmt)
                        : "-";

                String descricao = (p.getBoleto() != null
                        && p.getBoleto().getUnidade() != null)
                        ? "Apto " + p.getBoleto().getUnidade().getNumero()
                        : "Pagamento";

                String forma = p.getFormaPagamento() != null
                        ? p.getFormaPagamento() : "-";

                linhas.add(new LancamentoRow(
                        data,
                        descricao + " (" + forma + ")",
                        "Taxa Condominial",
                        "RECEITA",
                        moeda(p.getValorPago()),
                        moeda((float) saldoAcum)));
            }
        }

        if (balancete.getDespesas() != null) {
            for (Despesa d : balancete.getDespesas()) {
                saldoAcum -= d.getValor();

                String data = d.getDataVencimento() != null
                        ? d.getDataVencimento().format(fmt)
                        : "-";

                String tipo = d.getTipo() != null
                        ? d.getTipo().toString() : "-";

                String desc = d.getDescricao() != null
                        ? d.getDescricao() : "-";

                linhas.add(new LancamentoRow(
                        data,
                        desc,
                        tipo,
                        "DESPESA",
                        moeda(d.getValor()),
                        moeda((float) saldoAcum)));
            }
        }

        return linhas;
    }

    /** Formata float como moeda: R$ 1.234,56 */
    private String moeda(float valor) {
        String s = String.format("R$ %,.2f", valor);
        return s.replace(",", "X").replace(".", ",").replace("X", ".");
    }

    // =========================================================================
    // DTO interno para o datasource do JasperReports
    // JasperReports acessa os campos via getters (Java Beans)
    // =========================================================================

    public static class LancamentoRow {

        private final String data;
        private final String descricao;
        private final String tipo;
        private final String categoria;
        private final String valor;
        private final String saldoAcum;

        public LancamentoRow(String data, String descricao,
                             String tipo, String categoria,
                             String valor, String saldoAcum) {
            this.data      = data;
            this.descricao = descricao;
            this.tipo      = tipo;
            this.categoria = categoria;
            this.valor     = valor;
            this.saldoAcum = saldoAcum;
        }

        public String getData()      { return data; }
        public String getDescricao() { return descricao; }
        public String getTipo()      { return tipo; }
        public String getCategoria() { return categoria; }
        public String getValor()     { return valor; }
        public String getSaldoAcum() { return saldoAcum; }
    }
}