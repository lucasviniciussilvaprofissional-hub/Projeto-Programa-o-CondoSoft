package com.condominio.model;

public class Pet
{
    private int id;
    private String nome;
    private String tipo;
    private String raca;
    private String cor;


    public Pet(int id, String nome, String tipo, String raca, String cor, Unidade unidade) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.raca = raca;
        this.cor = cor;
    }

    //Setters


    public void setCor(String cor) {
        this.cor = cor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    //Getters


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

    public String getTipo() {
        return tipo;
    }
}
