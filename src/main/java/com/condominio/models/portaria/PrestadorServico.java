package com.condominio.models.portaria;

import com.condominio.models.moradia.Unidade;

public class PrestadorServico
{
    private int id;
    private String nome;
    private String cpf;
    private String empresa;
    private  String tipoServico;
    private String telefone;
    private Unidade unidadeDestino;

    public PrestadorServico(int id, String nome, String cpf, String empresa, String tipoServico, String telefone, Unidade unidadeDestino)
    {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.empresa = empresa;
        this.tipoServico = tipoServico;
        this.telefone = telefone;
        this.unidadeDestino = unidadeDestino;
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
        return telefone;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public Unidade getUnidadeDestino() {
        return unidadeDestino;
    }


    //Setters


    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public void setUnidadeDestino(Unidade unidadeDestino) {
        this.unidadeDestino = unidadeDestino;
    }
}
