package com.condominio.model;

public abstract class Morador
{
    protected int  id;
    protected String nome;
    protected String cpf;
    protected String telefone;

    public Morador(int id, String nome, String cpf, String telefone, Unidade unidade)
    {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    //Setters


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
        return null;
    }

    public void set(int i, Morador morador) {
    }
}
