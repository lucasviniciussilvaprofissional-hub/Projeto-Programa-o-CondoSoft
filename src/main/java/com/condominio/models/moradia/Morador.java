package com.condominio.models.moradia;

public abstract class Morador
{
    protected int  id;
    protected String nome;
    protected String cpf;
    protected String telefone;
    protected Unidade unidade;
    protected String email;

    public Morador(int id, String nome, String cpf, String telefone, Unidade unidade, String email)
    {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.unidade = unidade;
    }

    //Setters


    public void setEmail(String email) {
        this.email = email;
    }

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


    public String getEmail() {
        return email;
    }

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
        return null;
    }

    public void set(int i, Morador morador) {
    }
}
