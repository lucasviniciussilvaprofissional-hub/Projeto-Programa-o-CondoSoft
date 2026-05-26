package com.condominio.models.moradia;

import com.condominio.enums.TipoPet;

public class Pet {

    private int id;
    private String nome;
    private TipoPet tipo;
    private String raca;
    private String cor;
    private Unidade unidade;

    // =========================
    // CONSTRUTOR
    // =========================

    public Pet(int id,
               String nome,
               TipoPet tipo,
               String raca,
               String cor) {

        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.raca = raca;
        this.cor = cor;

    }

    // =========================
    // SETTERS
    // =========================

    public void setCor(String cor) {

        if (cor == null || cor.isEmpty()) {

            throw new IllegalArgumentException("Cor inválida.");

        }

        this.cor = cor;

    }

    public void setId(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException("ID inválido.");

        }

        this.id = id;

    }

    public void setNome(String nome) {

        if (nome == null || nome.isEmpty()) {

            throw new IllegalArgumentException("Nome inválido.");

        }

        this.nome = nome;

    }

    public void setRaca(String raca) {

        if (raca == null || raca.isEmpty()) {

            throw new IllegalArgumentException("Raça inválida.");

        }

        this.raca = raca;

    }

    public void setTipo(TipoPet tipo) {

        if (tipo == null) {

            throw new IllegalArgumentException("Tipo inválido.");

        }

        this.tipo = tipo;

    }

    public void setUnidade(Unidade unidade) {

        if (unidade == null) {

            throw new IllegalArgumentException("Unidade inválida.");

        }

        this.unidade = unidade;

    }

    // =========================
    // GETTERS
    // =========================

    public Unidade getUnidade() {
        return unidade;
    }

    public String getCor() {
        return cor;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public TipoPet getTipo() {
        return tipo;
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    public boolean possuiUnidade() {

        return unidade != null;

    }

    public void removerUnidade() {

        this.unidade = null;

    }

    public boolean petValido() {

        return nome != null
                && !nome.isEmpty()
                && raca != null
                && !raca.isEmpty();

    }

    public boolean ehCachorro() {

        return tipo == TipoPet.CACHORRO;

    }

    public boolean ehGato() {

        return tipo == TipoPet.GATO;

    }

}