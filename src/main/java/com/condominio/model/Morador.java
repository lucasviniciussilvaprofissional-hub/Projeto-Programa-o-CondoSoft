package com.condominio.model;

public abstract class Morador
{
    private int  id;
    private String nome;
    private String cpf;
    private String telefone;
    private Unidade unidade;

    public Morador(int id, String nome, String cpf, String telefone, Unidade unidade)
    {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.unidade = unidade;
    }

    //Setters


    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    //Getters


    public Unidade getUnidade() {
        return unidade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }
}
