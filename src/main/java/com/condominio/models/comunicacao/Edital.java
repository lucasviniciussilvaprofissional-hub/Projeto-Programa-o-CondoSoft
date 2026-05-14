package com.condominio.models.comunicacao;

import java.time.LocalDateTime;

public class Edital {

    private int id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataPublicacao;
    private LocalDateTime validade;
    private String status;

    public Edital(int id,
                  String titulo,
                  String descricao,
                  LocalDateTime validade) {

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.validade = validade;

        this.dataPublicacao = LocalDateTime.now();
        this.status = "ATIVO";
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}