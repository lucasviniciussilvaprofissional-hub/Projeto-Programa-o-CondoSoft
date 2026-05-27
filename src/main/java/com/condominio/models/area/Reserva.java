package com.condominio.models.area;

import com.condominio.models.area.AreaComum;
import com.condominio.models.area.Convidado;
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

    // =========================
    // CONSTRUTOR
    // =========================

    public Reserva(int id,
                   AreaComum area,
                   Unidade unidade,
                   Morador responsavel,
                   LocalDateTime dataInicio,
                   LocalDateTime dataFim,
                   int quantidadePessoas,
                   StatusReserva status) {

        setArea(area);
        setUnidade(unidade);
        setResponsavel(responsavel);
        setDataInicio(dataInicio);
        setDataFim(dataFim);
        setQuantidadePessoas(quantidadePessoas);
        setStatus(status);

        this.convidados = new ArrayList<>(convidados);

    }

    // =========================
    // SETTERS
    // =========================

    public void setId(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException("ID inválido.");

        }

        this.id = id;

    }

    public void setArea(AreaComum area) {

        if (area == null) {

            throw new IllegalArgumentException("Área inválida.");

        }

        this.area = area;

    }

    public void setUnidade(Unidade unidade) {

        if (unidade == null) {

            throw new IllegalArgumentException("Unidade inválida.");

        }

        this.unidade = unidade;

    }

    public void setDataFim(LocalDateTime dataFim) {

        if (dataFim.isBefore(dataInicio)) {

            throw new IllegalArgumentException("Data final inválida.");

        }

        this.dataFim = dataFim;

    }

    public void setDataInicio(LocalDateTime dataInicio) {

        if (dataInicio.isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException("Data inicial inválida.");

        }

        this.dataInicio = dataInicio;

    }

    public void setQuantidadePessoas(int quantidadePessoas) {

        if (quantidadePessoas <= 0) {

            throw new IllegalArgumentException("Quantidade inválida.");

        }

        this.quantidadePessoas = quantidadePessoas;

    }

    public void setResponsavel(Morador responsavel) {

        if (responsavel == null) {

            throw new IllegalArgumentException("Responsável inválido.");

        }

        this.responsavel = responsavel;

    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    // =========================
    // GETTERS
    // =========================

    public Unidade getUnidade() {
        return unidade;
    }

    public int getId() {
        return id;
    }

    public AreaComum getArea() {
        return area;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public Morador getResponsavel() {
        return responsavel;
    }

    public List<Convidado> getConvidados() {
        return convidados;
    }

    public StatusReserva getStatus() {
        return status;
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    public void adicionarConvidado(int id, String nome, String documento) {

        if (status == StatusReserva.CANCELADA) {

            throw new IllegalArgumentException("Reserva cancelada.");

        }

        if (quantidadePessoas >= area.getCapacidadeMaxima()) {

            throw new IllegalArgumentException("Capacidade máxima atingida.");

        }

        Convidado convidado = new Convidado(id, nome, documento);

        this.convidados.add(convidado);

        quantidadePessoas++;

    }

    public void cancelarReserva() {

        if (status == StatusReserva.FINALIZADA) {

            throw new IllegalArgumentException("Reserva já finalizada.");

        }

        status = StatusReserva.CANCELADA;

    }

    public void finalizarReserva() {

        if (status == StatusReserva.CANCELADA) {

            throw new IllegalArgumentException("Reserva cancelada.");

        }

        status = StatusReserva.FINALIZADA;

    }

    public boolean reservaAtiva() {

        return status == StatusReserva.ATIVA;

    }

    public void validarDatas() {

        if (dataFim.isBefore(dataInicio)) {

            throw new IllegalArgumentException("Período da reserva inválido.");

        }

    }

}