package com.condominio.models.disciplina;

import com.condominio.enums.StatusOcorrencia;
import com.condominio.enums.TipoOcorrencia;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;

import java.time.LocalDateTime;

public class Ocorrencia {

    private int id;
    private String titulo;
    private String descricao;

    private TipoOcorrencia tipo;
    private StatusOcorrencia status;

    private Morador responsavel;
    private Unidade unidade;

    private LocalDateTime dataCriacao;

    // =========================
    // CONSTRUTOR
    // =========================

    public Ocorrencia(int id,
                      String titulo,
                      String descricao,
                      TipoOcorrencia tipo,
                      StatusOcorrencia status,
                      Morador responsavel,
                      Unidade unidade,
                      LocalDateTime dataCriacao) {

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.status = status;
        this.responsavel = responsavel;
        this.unidade = unidade;
        this.dataCriacao = dataCriacao;

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

    public void setTitulo(String titulo) {

        if (titulo == null || titulo.isEmpty()) {

            throw new IllegalArgumentException("Título inválido.");

        }

        this.titulo = titulo;

    }

    public void setDescricao(String descricao) {

        if (descricao == null || descricao.isEmpty()) {

            throw new IllegalArgumentException("Descrição inválida.");

        }

        this.descricao = descricao;

    }

    public void setTipo(TipoOcorrencia tipo) {

        if (tipo == null) {

            throw new IllegalArgumentException("Tipo inválido.");

        }

        this.tipo = tipo;

    }

    public void setStatus(StatusOcorrencia status) {

        if (status == null) {

            throw new IllegalArgumentException("Status inválido.");

        }

        this.status = status;

    }

    public void setResponsavel(Morador responsavel) {

        if (responsavel == null) {

            throw new IllegalArgumentException("Responsável inválido.");

        }

        this.responsavel = responsavel;

    }

    public void setUnidade(Unidade unidade) {

        if (unidade == null) {

            throw new IllegalArgumentException("Unidade inválida.");

        }

        this.unidade = unidade;

    }

    public void setDataCriacao(LocalDateTime dataCriacao) {

        if (dataCriacao == null) {

            throw new IllegalArgumentException("Data inválida.");

        }

        this.dataCriacao = dataCriacao;

    }

    // =========================
    // GETTERS
    // =========================

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoOcorrencia getTipo() {
        return tipo;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public Morador getResponsavel() {
        return responsavel;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    public boolean ocorrenciaAtiva() {

        return status == StatusOcorrencia.ABERTA
                || status == StatusOcorrencia.EM_ANDAMENTO;

    }

    public void iniciarAtendimento() {

        if (status == StatusOcorrencia.CANCELADA) {

            throw new IllegalArgumentException("Ocorrência cancelada.");

        }

        if (status == StatusOcorrencia.FINALIZADA) {

            throw new IllegalArgumentException("Ocorrência já finalizada.");

        }

        status = StatusOcorrencia.EM_ANDAMENTO;

    }

    public void finalizarOcorrencia() {

        if (status == StatusOcorrencia.CANCELADA) {

            throw new IllegalArgumentException("Ocorrência cancelada.");

        }

        status = StatusOcorrencia.FINALIZADA;

    }

    public void cancelarOcorrencia() {

        if (status == StatusOcorrencia.FINALIZADA) {

            throw new IllegalArgumentException("Ocorrência já finalizada.");

        }

        status = StatusOcorrencia.CANCELADA;

    }

}