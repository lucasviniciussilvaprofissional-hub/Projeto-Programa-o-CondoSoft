package com.condominio.models.moradia;

public class Veiculo {

    private int id;
    private String modelo;
    private String placa;
    private String cor;

    private Unidade unidade;

    // =========================
    // CONSTRUTOR
    // =========================

    public Veiculo(int id,
                   String modelo,
                   String placa,
                   String cor,
                   Unidade unidade) {

        this.id = id;
        this.modelo = modelo;
        this.placa = placa;
        this.cor = cor;
        this.unidade = unidade;

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

    public void setModelo(String modelo) {

        if (modelo == null || modelo.isEmpty()) {

            throw new IllegalArgumentException("Modelo inválido.");

        }

        this.modelo = modelo;

    }

    public void setPlaca(String placa) {

        if (placa == null || placa.length() < 7) {

            throw new IllegalArgumentException("Placa inválida.");

        }

        this.placa = placa;

    }

    public void setCor(String cor) {

        if (cor == null || cor.isEmpty()) {

            throw new IllegalArgumentException("Cor inválida.");

        }

        this.cor = cor;

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

    public int getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public String getCor() {
        return cor;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    public boolean placaValida() {

        return placa != null && placa.length() >= 7;

    }

    public void removerUnidade() {

        this.unidade = null;

    }

    public boolean possuiUnidade() {

        return unidade != null;

    }

}