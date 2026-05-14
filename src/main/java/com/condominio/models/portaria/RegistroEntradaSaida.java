package com.condominio.models.portaria;

import com.condominio.models.moradia.Unidade;

import java.time.LocalDateTime;

public class RegistroEntradaSaida {

    private int id;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
    private String status;

    private Visitante visitante;
    private PrestadorServico prestadorServico;
    private Unidade unidade;

    public RegistroEntradaSaida(int id,
                                LocalDateTime dataHoraEntrada,
                                Unidade unidade) {

        this.id = id;
        this.dataHoraEntrada = dataHoraEntrada;
        this.unidade = unidade;
        this.status = "EM_ANDAMENTO";
    }

    //Setters


    public void setId(int id) {
        this.id = id;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
        this.dataHoraEntrada = dataHoraEntrada;
    }

    public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrestadorServico(PrestadorServico prestadorServico) {
        this.prestadorServico = prestadorServico;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }

    //Getters


    public int getId() {
        return id;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public String getStatus() {
        return status;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public PrestadorServico getPrestadorServico() {
        return prestadorServico;
    }

    public Visitante getVisitante() {
        return visitante;
    }
}
