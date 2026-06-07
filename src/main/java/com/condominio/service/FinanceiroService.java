package com.condominio.service;

import com.condominio.enums.StatusBoleto;
import com.condominio.enums.StatusDespesa;
import com.condominio.enums.TipoDespesa;
import com.condominio.models.finance.*;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.interfaces.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa REQ05, REQ06 e REQ07.
 *
 * REQ05 — Registrar despesas (Fixas e Extraordinárias) via herança.
 * REQ06 — Calcular rateio mensal por fração ideal.
 * REQ07 — Registrar pagamentos de boletos; controlar histórico de inadimplência.
 */
public class FinanceiroService {

    private final IBoletoRepository    boletoRepo;
    private final IDespesaRepository   despesaRepo;
    private final IRateioRepository    rateioRepo;
    private final IPagamentoRepository pagamentoRepo;
    private final IInadimplenciaRepository inadimplenciaRepo;

    public FinanceiroService(IBoletoRepository boletoRepo,
                             IDespesaRepository despesaRepo,
                             IRateioRepository rateioRepo,
                             IPagamentoRepository pagamentoRepo,
                             IInadimplenciaRepository inadimplenciaRepo) {
        this.boletoRepo        = boletoRepo;
        this.despesaRepo       = despesaRepo;
        this.rateioRepo        = rateioRepo;
        this.pagamentoRepo     = pagamentoRepo;
        this.inadimplenciaRepo = inadimplenciaRepo;
    }

    // =========================================================================
    // REQ05 — DESPESAS (herança: DespesaFixa / DespesaExtraordinaria)
    // =========================================================================

    /** Registra uma DespesaFixa (ex.: água, energia, portaria). */
    public DespesaFixa registrarDespesaFixa(String descricao,
                                             float valor,
                                             LocalDate vencimento,
                                             String periodicidade) {
        validarDescricao(descricao);
        validarValor(valor);

        DespesaFixa d = new DespesaFixa();
        d.setId(proximoIdDespesa());
        d.setDescricao(descricao);
        d.setValor(valor);
        d.setDataVencimento(vencimento);
        d.setPeriodicidade(periodicidade);
        d.setTipo(TipoDespesa.FIXA);
        d.setStatus(StatusDespesa.PENDENTE);
        d.setDataCriacao(LocalDateTime.now());

        despesaRepo.salvar(d);
        return d;
    }

    /** Registra uma DespesaExtraordinaria (ex.: reforma, emergência). */
    public DespesaExtraordinaria registrarDespesaExtraordinaria(String descricao,
                                                                  float valor,
                                                                  LocalDate vencimento,
                                                                  String motivo) {
        validarDescricao(descricao);
        validarValor(valor);

        DespesaExtraordinaria d = new DespesaExtraordinaria();
        d.setId(proximoIdDespesa());
        d.setDescricao(descricao);
        d.setValor(valor);
        d.setDataVencimento(vencimento);
        d.setMotivo(motivo);
        d.setTipo(TipoDespesa.EXTRAORDINARIA);
        d.setStatus(StatusDespesa.PENDENTE);
        d.setDataCriacao(LocalDateTime.now());

        despesaRepo.salvar(d);
        return d;
    }

    public List<Despesa> listarDespesas()                              { return despesaRepo.listar(); }
    public List<Despesa> listarDespesasPorStatus(StatusDespesa status) { return despesaRepo.buscarPorStatus(status); }
    public List<Despesa> listarDespesasFixas()                         { return despesaRepo.buscarPorTipo("FIXA"); }
    public List<Despesa> listarDespesasExtraordinarias()               { return despesaRepo.buscarPorTipo("EXTRAORDINARIA"); }

    public void marcarDespesaPaga(int despesaId) {
        Despesa d = despesaRepo.buscarPorId(despesaId);
        if (d == null) throw new IllegalArgumentException("Despesa não encontrada.");
        d.setStatus(StatusDespesa.PAGA);
        despesaRepo.atualizar(d);
    }

    // =========================================================================
    // REQ06 — RATEIO MENSAL
    // =========================================================================

    /**
     * Calcula e persiste o rateio de uma despesa entre as unidades informadas,
     * proporcional à fração ideal de cada uma.
     *
     * @param despesa   despesa a ser rateada
     * @param unidades  lista de unidades participantes
     * @param competencia mês de referência (ex.: "06/2026")
     * @return lista de Rateio gerada
     */
    public List<Rateio> calcularRateio(Despesa despesa,
                                        List<Unidade> unidades,
                                        String competencia) {
        if (despesa == null) throw new IllegalArgumentException("Despesa obrigatória.");
        if (unidades == null || unidades.isEmpty())
            throw new IllegalArgumentException("Nenhuma unidade informada.");
        if (competencia == null || competencia.isBlank())
            throw new IllegalArgumentException("Competência obrigatória.");

        // soma das frações para normalização
        double somaFracoes = unidades.stream()
                .mapToDouble(Unidade::getFracaoIdeal).sum();

        if (somaFracoes <= 0)
            throw new IllegalArgumentException("Soma das frações ideais inválida.");

        List<Rateio> resultado = new ArrayList<>();
        int seq = rateioRepo.listar().size() + 1;

        for (Unidade u : unidades) {
            float valorRateado = (float) (despesa.getValor()
                    * (u.getFracaoIdeal() / somaFracoes));

            Rateio r = new Rateio();
            r.setId(seq++);
            r.setDespesa(despesa);
            r.setUnidade(u);
            r.setValorRateado(valorRateado);
            r.setCompetencia(competencia);

            rateioRepo.salvar(r);
            resultado.add(r);
        }

        return resultado;
    }

    public List<Rateio> listarRateiosPorUnidade(int unidadeId) {
        return rateioRepo.listarPorUnidade(unidadeId);
    }

    public List<Rateio> listarRateiosPorCompetencia(String competencia) {
        return rateioRepo.listarPorCompetencia(competencia);
    }

    // =========================================================================
    // REQ07 — BOLETOS, PAGAMENTOS E INADIMPLÊNCIA
    // =========================================================================

    /** Gera um boleto para uma unidade a partir de um rateio calculado. */
    public Boleto gerarBoleto(Unidade unidade, float valor,
                               String competencia, LocalDate vencimento) {
        if (unidade == null) throw new IllegalArgumentException("Unidade obrigatória.");
        validarValor(valor);

        int novoId = boletoRepo.listar().size() + 1;
        String codigo = String.format("BOL%06d-U%d-%d",
                novoId, unidade.getId(), System.currentTimeMillis() % 10000);

        Boleto b = new Boleto(novoId, codigo, valor,
                competencia, vencimento, StatusBoleto.PENDENTE);
        b.setUnidade(unidade);
        boletoRepo.salvar(b);
        return b;
    }

    /** Gera boletos em lote para todas as unidades baseado no total do condomínio. */
    public List<Boleto> gerarBoletosTodos(List<Unidade> unidades,
                                           float totalCondominio,
                                           String competencia,
                                           LocalDate vencimento) {
        List<Boleto> gerados = new ArrayList<>();
        double somaFracoes = unidades.stream().mapToDouble(Unidade::getFracaoIdeal).sum();

        for (Unidade u : unidades) {
            float valor = (float) (totalCondominio * (u.getFracaoIdeal() / somaFracoes));
            gerados.add(gerarBoleto(u, valor, competencia, vencimento));
        }
        return gerados;
    }

    /**
     * Registra o pagamento de um boleto.
     * Se o boleto estava vencido, remove da inadimplência.
     */
    public Pagamento registrarPagamento(int boletoId, String formaPagamento) {
        Boleto boleto = boletoRepo.buscarPorId(boletoId);
        if (boleto == null) throw new IllegalArgumentException("Boleto não encontrado.");
        if (boleto.getStatus() == StatusBoleto.PAGO)
            throw new IllegalArgumentException("Boleto já está pago.");
        if (boleto.getStatus() == StatusBoleto.CANCELADO)
            throw new IllegalArgumentException("Não é possível pagar boleto cancelado.");

        boleto.pagarBoleto();
        boletoRepo.atualizar(boleto);

        int novoId = pagamentoRepo.listar().size() + 1;
        Pagamento p = new Pagamento();
        p.setId(novoId);
        p.setValorPago(boleto.getValor());
        p.setFormaPagamento(formaPagamento);
        p.setDataPagamento(LocalDateTime.now());
        p.setBoleto(boleto);
        pagamentoRepo.salvar(p);

        // Remove inadimplência correspondente, se houver
        for (Inadimplencia i : inadimplenciaRepo.listar()) {
            if (i.getBoleto() != null && i.getBoleto().getId() == boletoId) {
                i.setStatus("REGULARIZADO");
                inadimplenciaRepo.atualizar(i);
            }
        }

        return p;
    }

    /**
     * Verifica boletos vencidos e registra inadimplência automaticamente.
     * Deve ser chamado no initialize() do dashboard.
     */
    public void verificarInadimplencias() {
        for (Boleto b : boletoRepo.listar()) {
            // 1. Se o boleto estiver PENDENTE mas a data passou, atualiza para VENCIDO
            if (b.getStatus() == StatusBoleto.PENDENTE && b.boletoVencido()) {
                b.setStatus(StatusBoleto.VENCIDO);
                boletoRepo.atualizar(b);
            }

            // 2. Agora, se o boleto for VENCIDO (ou porque foi atualizado acima, ou porque nasceu vencido)
            if (b.getStatus() == StatusBoleto.VENCIDO) {

                // Só registra se ainda não existe inadimplência para este boleto
                boolean jaRegistrado = inadimplenciaRepo.listar().stream()
                        .anyMatch(i -> i.getBoleto() != null && i.getBoleto().getId() == b.getId());

                if (!jaRegistrado && b.getUnidade() != null) {
                    // Calcula os dias de atraso de forma segura
                    int dias = (int) (LocalDate.now().toEpochDay() - b.getDataVencimento().toEpochDay());
                    // Garante que o número de dias não seja negativo caso haja alguma inconsistência
                    if (dias < 0) dias = 0;

                    float multa = (float) b.calcularMulta();

                    Inadimplencia inadimplencia = new Inadimplencia(
                            inadimplenciaRepo.listar().size() + 1,
                            b.getValor() + multa,
                            dias,
                            b,
                            b.getUnidade()
                    );
                    inadimplenciaRepo.salvar(inadimplencia);
                }
            }
        }
    }

    // ── consultas ─────────────────────────────────────────────────────────
    public List<Boleto> listarBoletos()           { return boletoRepo.listar(); }
    public List<Boleto> listarBoletosPorUnidade(int id) { return boletoRepo.listarPorUnidade(id); }
    public List<Boleto> listarBoletosPendentes()  { return boletoRepo.listarPorStatus(StatusBoleto.PENDENTE); }
    public List<Boleto> listarBoletosPagos()      { return boletoRepo.listarPorStatus(StatusBoleto.PAGO); }
    public List<Boleto> listarBoletosVencidos()   { return boletoRepo.listarPorStatus(StatusBoleto.VENCIDO); }
    public List<Pagamento> listarPagamentos()     { return pagamentoRepo.listar(); }
    public List<Inadimplencia> listarInadimplencias() { return inadimplenciaRepo.listar(); }
    public List<Inadimplencia> listarInadimplenciasPendentes() {
        List<Inadimplencia> r = new ArrayList<>();
        for (Inadimplencia i : inadimplenciaRepo.listar())
            if ("PENDENTE".equals(i.getStatus())) r.add(i);
        return r;
    }

    // ── totais para dashboard ─────────────────────────────────────────────
    public float totalArrecadado() {
        return (float) listarBoletosPagos().stream().mapToDouble(Boleto::getValor).sum();
    }

    public float totalInadimplencia() {
        return (float) listarInadimplenciasPendentes()
                .stream().mapToDouble(Inadimplencia::getValorDevido).sum();
    }

    public int qtdBoletosAbertos() {
        return listarBoletosPendentes().size() + listarBoletosVencidos().size();
    }

    // ── util privado ──────────────────────────────────────────────────────
    private int proximoIdDespesa() { return despesaRepo.listar().size() + 1; }
    private void validarValor(float v) {
        if (v <= 0) throw new IllegalArgumentException("Valor deve ser positivo.");
    }
    private void validarDescricao(String d) {
        if (d == null || d.isBlank()) throw new IllegalArgumentException("Descrição obrigatória.");
    }
}
