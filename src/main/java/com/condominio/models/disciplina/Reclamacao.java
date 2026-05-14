package com.condominio.models.disciplina;

import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;

import java.time.LocalDateTime;

public class Reclamacao {

    private int id;
    private String titulo;
    private String descricao;
    private String status;
    private LocalDateTime dataRegistro;
    private Morador morador;
    private Unidade unidade;

    public Reclamacao(int id,
                      String titulo,
                      String descricao,
                      Morador morador,
                      Unidade unidade) {

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.morador = morador;
        this.unidade = unidade;
        this.dataRegistro = LocalDateTime.now();
        this.status = "ABERTA";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
}