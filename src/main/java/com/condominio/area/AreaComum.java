package com.condominio.area;

public class AreaComum
{
    private int id;
    private String nome;
    private int capacidadeMaxima;
    private String descricao;

    public AreaComum(int id, String nome, int capacidadeMaxima, String descricao)
    {
        this.id = id;
        this.nome = nome;
        this.capacidadeMaxima = capacidadeMaxima;
        this.descricao = descricao;
    }

    //Setters


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    //Getters


    public String getDescricao() {
        return descricao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
}
