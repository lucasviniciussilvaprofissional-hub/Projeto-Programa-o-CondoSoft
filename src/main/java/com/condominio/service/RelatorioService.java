package com.condominio.service;

import com.condominio.models.finance.Despesa;
import com.condominio.models.finance.Inadimplencia;
import com.condominio.models.finance.Pagamento;
import com.condominio.models.relatorio.Balancete;
import com.condominio.repository.interfaces.IBoletoRepository;
import com.condominio.repository.interfaces.IDespesaRepository;
import com.condominio.repository.interfaces.IInadimplenciaRepository;
import com.condominio.repository.interfaces.IPagamentoRepository;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RelatorioService {

    private static final DeviceRgb COR_AZUL_ESCURO = new DeviceRgb(30,  80, 160);
    private static final DeviceRgb COR_AZUL_MEDIO  = new DeviceRgb(52, 120, 200);
    private static final DeviceRgb COR_LINHA_PAR   = new DeviceRgb(235, 242, 252);
    private static final DeviceRgb COR_VERDE       = new DeviceRgb(30,  130,  76);
    private static final DeviceRgb COR_VERMELHO    = new DeviceRgb(180,  30,  30);
    private static final DeviceRgb COR_TOTAL_BG    = new DeviceRgb(220, 230, 245);
    private static final DeviceRgb COR_BORDA_CARD  = new DeviceRgb(200, 210, 230);

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
    // Geracao do Balancete
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
    // REQ14 - Gerar PDF real do balancete via iText 7
    // =========================================================================

    public String gerarPdfBalancete(Balancete balancete, String pastaDestino) throws IOException {

        String nomeArquivo = "Balancete_"
                + balancete.getPeriodo().replace("/", "_")
                + ".pdf";

        Path destino = Path.of(pastaDestino, nomeArquivo);

        try (PdfWriter   writer = new PdfWriter(destino.toFile());
             PdfDocument pdf    = new PdfDocument(writer);
             Document    doc    = new Document(pdf, PageSize.A4)) {

            doc.setMargins(36, 36, 36, 36);

            PdfFont bold   = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // --- Cabecalho ---------------------------------------------------
            doc.add(new Paragraph("CONDOSOFT")
                    .setFont(bold)
                    .setFontSize(22)
                    .setFontColor(COR_AZUL_ESCURO)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph("Balancete Mensal - " + balancete.getPeriodo())
                    .setFont(bold)
                    .setFontSize(14)
                    .setFontColor(COR_AZUL_MEDIO)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(4));

            String dataHoje = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            doc.add(new Paragraph("Gerado em: " + dataHoje)
                    .setFont(normal)
                    .setFontSize(9)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(16));

            // --- Cards de resumo ---------------------------------------------
            Table resumo = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);

            resumo.addCell(cardResumo("Total de Receitas",
                    moeda(balancete.getTotalReceitas()), COR_VERDE, bold, normal));
            resumo.addCell(cardResumo("Total de Despesas",
                    moeda(balancete.getTotalDespesas()), COR_VERMELHO, bold, normal));

            float saldo = balancete.getSaldoFinal();
            resumo.addCell(cardResumo("Saldo Final",
                    moeda(saldo), saldo >= 0 ? COR_VERDE : COR_VERMELHO, bold, normal));

            doc.add(resumo);

            // --- Tabela de receitas ------------------------------------------
            doc.add(secao("Receitas - Pagamentos Recebidos", bold));

            List<Pagamento> pagamentos = balancete.getPagamentos();
            if (pagamentos == null || pagamentos.isEmpty()) {
                doc.add(vazio("Nenhum pagamento registrado neste periodo.", normal));
            } else {
                Table t = tabela(new String[]{"#", "Data", "Forma de Pagamento", "Valor (R$)"},
                        new float[] { 5,  20,      50,                   25 }, bold);

                for (int i = 0; i < pagamentos.size(); i++) {
                    Pagamento p = pagamentos.get(i);
                    boolean par = i % 2 == 0;
                    String data = p.getDataPagamento() != null
                            ? p.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            : "-";
                    String forma = p.getFormaPagamento() != null ? p.getFormaPagamento() : "-";

                    t.addCell(celula(String.valueOf(i + 1), normal, par, TextAlignment.CENTER));
                    t.addCell(celula(data,  normal, par, TextAlignment.CENTER));
                    t.addCell(celula(forma, normal, par, TextAlignment.LEFT));
                    t.addCell(celula(moeda(p.getValorPago()), normal, par, TextAlignment.RIGHT));
                }

                t.addCell(totalLabel(3, "Total Receitas", bold));
                t.addCell(totalValor(moeda(balancete.getTotalReceitas()), bold, COR_VERDE));
                doc.add(t.setMarginBottom(16));
            }

            // --- Tabela de despesas ------------------------------------------
            doc.add(secao("Despesas Lancadas", bold));

            List<Despesa> despesas = balancete.getDespesas();
            if (despesas == null || despesas.isEmpty()) {
                doc.add(vazio("Nenhuma despesa registrada neste periodo.", normal));
            } else {
                Table t = tabela(new String[]{"#", "Descricao", "Tipo", "Vencimento", "Valor (R$)"},
                        new float[] { 5,   40,          15,     18,            22 }, bold);

                for (int i = 0; i < despesas.size(); i++) {
                    Despesa d = despesas.get(i);
                    boolean par = i % 2 == 0;
                    String venc = d.getDataVencimento() != null
                            ? d.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            : "-";
                    String tipo = d.getTipo() != null ? d.getTipo().toString() : "-";
                    String desc = d.getDescricao() != null ? d.getDescricao() : "-";

                    t.addCell(celula(String.valueOf(i + 1), normal, par, TextAlignment.CENTER));
                    t.addCell(celula(desc, normal, par, TextAlignment.LEFT));
                    t.addCell(celula(tipo, normal, par, TextAlignment.CENTER));
                    t.addCell(celula(venc, normal, par, TextAlignment.CENTER));
                    t.addCell(celula(moeda(d.getValor()), normal, par, TextAlignment.RIGHT));
                }

                t.addCell(totalLabel(4, "Total Despesas", bold));
                t.addCell(totalValor(moeda(balancete.getTotalDespesas()), bold, COR_VERMELHO));
                doc.add(t.setMarginBottom(16));
            }

            // --- Rodape ------------------------------------------------------
            doc.add(new Paragraph(
                    "Documento gerado automaticamente pelo sistema CondoSoft. "
                            + "Competencia: " + balancete.getPeriodo() + ".")
                    .setFont(normal)
                    .setFontSize(8)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(16));
        }

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
    // Helpers privados de layout
    // =========================================================================

    private Cell cardResumo(String titulo, String valor,
                            DeviceRgb corValor, PdfFont bold, PdfFont normal) {
        Cell c = new Cell()
                .setPadding(10)
                .setBorder(new SolidBorder(COR_BORDA_CARD, 1));
        c.add(new Paragraph(titulo)
                .setFont(normal).setFontSize(9)
                .setFontColor(ColorConstants.GRAY).setMarginBottom(4));
        c.add(new Paragraph(valor)
                .setFont(bold).setFontSize(15)
                .setFontColor(corValor));
        return c;
    }

    private Paragraph secao(String texto, PdfFont bold) {
        return new Paragraph(texto)
                .setFont(bold).setFontSize(11)
                .setFontColor(COR_AZUL_ESCURO).setMarginBottom(4);
    }

    private Paragraph vazio(String texto, PdfFont normal) {
        return new Paragraph(texto)
                .setFont(normal).setFontSize(10)
                .setFontColor(ColorConstants.GRAY)
                .setItalic().setMarginBottom(16);
    }

    private Table tabela(String[] headers, float[] cols, PdfFont bold) {
        Table t = new Table(UnitValue.createPercentArray(cols))
                .setWidth(UnitValue.createPercentValue(100));
        for (String h : headers) {
            t.addHeaderCell(new Cell()
                    .setBackgroundColor(COR_AZUL_ESCURO)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(5)
                    .add(new Paragraph(h)
                            .setFont(bold).setFontSize(9)
                            .setFontColor(ColorConstants.WHITE)
                            .setTextAlignment(TextAlignment.CENTER)));
        }
        return t;
    }

    private Cell celula(String texto, PdfFont font, boolean par, TextAlignment align) {
        return new Cell()
                .setBackgroundColor(par ? COR_LINHA_PAR : ColorConstants.WHITE)
                .setBorder(Border.NO_BORDER)
                .setPadding(4)
                .add(new Paragraph(texto != null ? texto : "-")
                        .setFont(font).setFontSize(9)
                        .setTextAlignment(align));
    }

    private Cell totalLabel(int span, String label, PdfFont bold) {
        return new Cell(1, span)
                .setBackgroundColor(COR_TOTAL_BG)
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .add(new Paragraph(label)
                        .setFont(bold).setFontSize(9)
                        .setTextAlignment(TextAlignment.RIGHT));
    }

    private Cell totalValor(String valor, PdfFont bold, DeviceRgb cor) {
        return new Cell()
                .setBackgroundColor(COR_TOTAL_BG)
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .add(new Paragraph(valor)
                        .setFont(bold).setFontSize(9)
                        .setFontColor(cor)
                        .setTextAlignment(TextAlignment.RIGHT));
    }

    private String moeda(float valor) {
        // Formata como R$ 1.234,56
        String s = String.format("R$ %,.2f", valor);
        // String.format usa locale do sistema; normaliza para padrao BR
        s = s.replace(",", "X").replace(".", ",").replace("X", ".");
        return s;
    }
}