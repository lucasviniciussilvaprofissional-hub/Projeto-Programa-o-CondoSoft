package com.condominio.service;

import com.condominio.enums.StatusBoleto;
import com.condominio.enums.StatusReserva;
import com.condominio.models.area.AreaComum;
import com.condominio.models.area.Convidado;
import com.condominio.models.area.Reserva;
import com.condominio.models.area.TaxaLimpeza;
import com.condominio.models.finance.Boleto;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.interfaces.IBoletoRepository;
import com.condominio.repository.interfaces.IReservaRepository;
import com.condominio.repository.interfaces.ITaxaLimpezaRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * ReservaService — implementa REQ08 a REQ10 e REQ16/17/21.
 *
 * REQ08  Reservar espaços via agenda.
 * REQ09  Registrar lista de convidados para controle de acesso.
 * REQ10  Lançar taxa de limpeza automaticamente após finalizar reserva.
 * REQ16  Bloquear reserva para unidade com boleto em atraso.
 * REQ17  Não permitir reservas que excedam a capacidade máxima.
 * REQ21  Validar período de silêncio (22:00 – 08:00).
 */
public class ReservaService {

    // ── Período de silêncio REQ21 ─────────────────────────────────────────
    private static final LocalTime SILENCIO_INICIO = LocalTime.of(22, 0);
    private static final LocalTime SILENCIO_FIM    = LocalTime.of(8, 0);

    // ── Valor padrão da taxa de limpeza REQ10 ─────────────────────────────
    private static final double TAXA_LIMPEZA_PADRAO = 150.0;

    private final IReservaRepository      reservaRepo;
    private final ITaxaLimpezaRepository  taxaRepo;
    private final IBoletoRepository       boletoRepo;

    public ReservaService(IReservaRepository reservaRepo,
                          ITaxaLimpezaRepository taxaRepo,
                          IBoletoRepository boletoRepo) {
        this.reservaRepo = reservaRepo;
        this.taxaRepo    = taxaRepo;
        this.boletoRepo  = boletoRepo;
    }

    // =========================================================================
    // REQ08 — Criar reserva via agenda
    // =========================================================================
    public Reserva criarReserva(Morador responsavel,
                                AreaComum area,
                                LocalDateTime inicio,
                                LocalDateTime fim,
                                int qtdPessoas) {

        if (responsavel == null)
            throw new IllegalArgumentException("Responsável obrigatório para criar reserva.");

        Unidade unidade = responsavel.getUnidade();
        if (unidade == null)
            throw new IllegalArgumentException("Morador não possui unidade associada.");

        if (area == null)
            throw new IllegalArgumentException("Área comum obrigatória.");
        if (inicio == null || fim == null || !fim.isAfter(inicio))
            throw new IllegalArgumentException("Período da reserva inválido.");

        // REQ16 — bloqueia se há boleto vencido na unidade
        validarAdimplencia(unidade);

        // REQ17 — valida capacidade
        validarCapacidade(area, qtdPessoas);

        // REQ21 — valida período de silêncio
        validarPeriodoSilencio(inicio, fim);

        // REQ08 — valida conflito de horário
        validarConflito(area, inicio, fim, -1);

        int novoId = reservaRepo.listar().size() + 1;
        Reserva reserva = new Reserva(novoId, area, unidade, responsavel,
                inicio, fim, qtdPessoas, StatusReserva.ATIVA);
        reservaRepo.salvar(reserva);
        return reserva;
    }

    // =========================================================================
    // REQ09 — Adicionar convidado (lista de acesso)
    // =========================================================================
    public void adicionarConvidado(int reservaId, String nome, String documento) {
        Reserva reserva = reservaRepo.buscarPorId(reservaId);
        if (reserva == null)
            throw new IllegalArgumentException("Reserva não encontrada.");
        if (reserva.getStatus() != StatusReserva.ATIVA)
            throw new IllegalArgumentException("Só é possível adicionar convidados em reservas ATIVAS.");

        // REQ17 — verifica capacidade antes de adicionar convidado
        if (reserva.getQuantidadePessoas() >= reserva.getArea().getCapacidadeMaxima())
            throw new IllegalArgumentException(
                    "Capacidade máxima da área atingida ("
                    + reserva.getArea().getCapacidadeMaxima() + " pessoas).");

        int novoId = reserva.getConvidados().size() + 1;
        reserva.adicionarConvidado(novoId, nome, documento);
        reservaRepo.atualizar(reserva);
    }

    public List<Convidado> listarConvidados(int reservaId) {
        Reserva r = reservaRepo.buscarPorId(reservaId);
        if (r == null) throw new IllegalArgumentException("Reserva não encontrada.");
        return r.getConvidados();
    }

    // =========================================================================
    // REQ10 — Lançar taxa de limpeza ao finalizar reserva
    // =========================================================================
    public TaxaLimpeza finalizarReserva(int reservaId) {
        Reserva reserva = reservaRepo.buscarPorId(reservaId);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva nao encontrada.");
        }

        reserva.finalizarReserva();
        reservaRepo.atualizar(reserva);

        TaxaLimpeza taxa = new TaxaLimpeza(
                taxaRepo.listar().size() + 1,
                TAXA_LIMPEZA_PADRAO,
                reserva,
                false
        );
        taxaRepo.salvar(taxa);

        try {
            int novoBoletoId = boletoRepo.listar().size() + 1;

            // Adaptando os dados para os tipos exatos do seu construtor:
            String codigoBarrasFake = "34191.79001 01043.513184 91020.150008 7 970000000" + novoBoletoId;
            float valorFloat = (float) TAXA_LIMPEZA_PADRAO;

            // Define o mês/ano atual como competência (ex: "06/2026")
            java.time.LocalDate hoje = java.time.LocalDate.now();
            String competenciaAtual = String.format("%02d/%d", hoje.getMonthValue(), hoje.getYear());

            // Vencimento em LocalDate (daqui a 5 dias)
            java.time.LocalDate dataVencimento = hoje.plusDays(30);

            // Criando o objeto usando exatamente o seu construtor
            Boleto novoBoleto = new Boleto(
                    novoBoletoId,
                    codigoBarrasFake,
                    valorFloat,
                    competenciaAtual,
                    dataVencimento,
                    StatusBoleto.PENDENTE
            );
            novoBoleto.setUnidade(reserva.getUnidade());

            // Se a sua classe Boleto tiver um setter para Unidade (caso precise vincular ao morador),
            // você pode chamá-lo logo abaixo antes de salvar:
            // novoBoleto.setUnidade(reserva.getUnidade());

            boletoRepo.salvar(novoBoleto);
        } catch (Exception e) {
            System.err.println("Erro ao gerar boleto para o financeiro:");
            e.printStackTrace();
        }

        return taxa;
    }
    public void cancelarReserva(int reservaId) {
        Reserva r = reservaRepo.buscarPorId(reservaId);
        if (r == null) throw new IllegalArgumentException("Reserva não encontrada.");
        r.cancelarReserva();
        reservaRepo.atualizar(r);
    }

    // =========================================================================
    // Consultas
    // =========================================================================
    public List<Reserva> listarReservas()                           { return reservaRepo.listar(); }
    public List<Reserva> listarPorUnidade(int id)                   { return reservaRepo.listarPorUnidade(id); }
    public List<Reserva> listarPorArea(String nome)                 { return reservaRepo.listarPorArea(nome); }
    public List<Reserva> listarPorStatus(StatusReserva status)      { return reservaRepo.listarPorStatus(status); }
    public List<Reserva> listarPorMorador(int id)                   { return reservaRepo.listarPorMorador(id); }
    public List<TaxaLimpeza> listarTaxasLimpeza()                   { return taxaRepo.listar(); }

    /** Verifica se área está disponível sem lançar exceção (para badge de disponibilidade). */
    public boolean isDisponivel(AreaComum area, LocalDateTime inicio, LocalDateTime fim) {
        try { validarConflito(area, inicio, fim, -1); return true; }
        catch (IllegalArgumentException e) { return false; }
    }

    // =========================================================================
    // Validações privadas (regras de negócio)
    // =========================================================================

    /** REQ16 */
    private void validarAdimplencia(Unidade unidade) {
        for (Boleto b : boletoRepo.listar()) {
            if (b.getUnidade() != null
                    && b.getUnidade().getId() == unidade.getId()
                    && (b.getStatus() == StatusBoleto.VENCIDO
                        || (b.getStatus() == StatusBoleto.PENDENTE && b.boletoVencido()))) {
                throw new IllegalArgumentException(
                        "Unidade " + unidade.getNumero()
                        + " possui boleto em atraso. Regularize antes de reservar.");
            }
        }
    }

    /** REQ17 */
    private void validarCapacidade(AreaComum area, int qtd) {
        if (qtd > area.getCapacidadeMaxima())
            throw new IllegalArgumentException(
                    "Número de pessoas (" + qtd
                    + ") excede a capacidade da área ("
                    + area.getCapacidadeMaxima() + ").");
    }

    /** REQ21 */
    private void validarPeriodoSilencio(LocalDateTime inicio, LocalDateTime fim) {
        LocalTime hi = inicio.toLocalTime();
        LocalTime hf = fim.toLocalTime();

        boolean inicioNaSilencio = isHoraNoPeriodoSilencio(hi);
        boolean fimNaSilencio    = isHoraNoPeriodoSilencio(hf);

        if (inicioNaSilencio || fimNaSilencio) {
            throw new IllegalArgumentException(
                    "Reserva não permitida no período de silêncio (22:00 – 08:00).");
        }
    }

    private boolean isHoraNoPeriodoSilencio(LocalTime hora) {
        // período de silêncio cruza meia-noite: ≥22:00 OU <08:00
        return hora.compareTo(SILENCIO_INICIO) >= 0
            || hora.compareTo(SILENCIO_FIM) < 0;
    }

    /** REQ08 — conflito de horário para a mesma área */
    private void validarConflito(AreaComum area, LocalDateTime inicio, LocalDateTime fim, int idIgnorar) {
        for (Reserva r : reservaRepo.listarPorArea(area.getNome())) {
            if (r.getId() == idIgnorar) continue;
            if (r.getStatus() == StatusReserva.CANCELADA) continue;
            boolean sobreposicao = inicio.isBefore(r.getDataFim())
                    && fim.isAfter(r.getDataInicio());
            if (sobreposicao)
                throw new IllegalArgumentException(
                        "Conflito de horário: " + area.getNome()
                        + " já reservado das "
                        + r.getDataInicio().toLocalTime()
                        + " às " + r.getDataFim().toLocalTime() + ".");
        }
    }


}
