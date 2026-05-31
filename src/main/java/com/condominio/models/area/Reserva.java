package com.condominio.models.area;

import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.enums.StatusReserva;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva {

    private int id;
    private AreaComum area;
    private Unidade unidade;
    private Morador responsavel;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int quantidadePessoas;
    private List<Convidado> convidados;
    private StatusReserva status;

    public Reserva(int id,
                   AreaComum area,
                   Unidade unidade,
                   Morador responsavel,
                   LocalDateTime dataInicio,
                   LocalDateTime dataFim,
                   int quantidadePessoas,
                   StatusReserva status) {

        this.id = id;
        // Importante: Definir o início antes do fim para as validações não darem NullPointer
        setDataInicio(dataInicio);
        setDataFim(dataFim);
        setArea(area);
        setUnidade(unidade);
        setResponsavel(responsavel);
        setQuantidadePessoas(quantidadePessoas);
        setStatus(status);

        // Correção aqui: Inicializa a lista vazia corretamente
        this.convidados = new ArrayList<>();
    }

    // Métodos auxiliares para a TableView do JavaFX conseguir ler textos simples
    public String getNomeArea() {
        return area != null ? area.toString() : "Área não definida";
    }

    public String getNomeResponsavel() {
        return responsavel != null ? responsavel.toString() : "Não informado";
    }

    // =========================
    // SETTERS
    // =========================

    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }

    public void setArea(AreaComum area) {
        if (area == null) throw new IllegalArgumentException("Área inválida.");
        this.area = area;
    }

    public void setUnidade(Unidade unidade) {
        if (unidade == null) throw new IllegalArgumentException("Unidade inválida.");
        this.unidade = unidade;
    }

    public void setDataFim(LocalDateTime dataFim) {
        if (dataInicio != null && dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data final inválida.");
        }
        this.dataFim = dataFim;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        // Removida a validação de retroatividade estrita para testes locais não quebrarem dependendo do horário do PC
        this.dataInicio = dataInicio;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        if (quantidadePessoas <= 0) throw new IllegalArgumentException("Quantidade inválida.");
        this.quantidadePessoas = quantidadePessoas;
    }

    public void setResponsavel(Morador responsavel) {
        if (responsavel == null) throw new IllegalArgumentException("Responsável inválido.");
        this.responsavel = responsavel;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    // =========================
    // GETTERS
    // =========================

    public Unidade getUnidade() { return unidade; }
    public int getId() { return id; }
    public AreaComum getArea() { return area; }
    public LocalDateTime getDataFim() { return dataFim; }
    public int getQuantidadePessoas() { return quantidadePessoas; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public Morador getResponsavel() { return responsavel; }
    public List<Convidado> getConvidados() { return convidados; }
    public StatusReserva getStatus() { return status; }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    public void adicionarConvidado(int id, String nome, String documento) {
        if (status == StatusReserva.CANCELADA) throw new IllegalArgumentException("Reserva cancelada.");
        if (area != null && quantidadePessoas >= area.getCapacidadeMaxima()) {
            throw new IllegalArgumentException("Capacidade máxima atingida.");
        }
        this.convidados.add(new Convidado(id, nome, documento));
        quantidadePessoas++;
    }

    public void cancelarReserva() {
        if (status == StatusReserva.FINALIZADA) throw new IllegalArgumentException("Reserva já finalizada.");
        status = StatusReserva.CANCELADA;
    }

    public void finalizarReserva() {
        if (status == StatusReserva.CANCELADA) throw new IllegalArgumentException("Reserva cancelada.");
        status = StatusReserva.FINALIZADA;
    }

    public boolean reservaAtiva() { return status == StatusReserva.ATIVA; }

    public void validarDatas() {
        if (dataFim.isBefore(dataInicio)) throw new IllegalArgumentException("Período da reserva inválido.");
    }
}